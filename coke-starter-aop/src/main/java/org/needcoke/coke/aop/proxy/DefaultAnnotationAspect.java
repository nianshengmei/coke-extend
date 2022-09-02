package org.needcoke.coke.aop.proxy;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.*;
import org.needcoke.coke.aop.proxy.advice.*;

import java.lang.reflect.Method;

/**
 * 默认的注解切面
 */
@Data
public class DefaultAnnotationAspect implements Aspect {

    /**
     * 源bean
     */
    private Object aspectBean;

    /**
     * 前置通知
     */
    private Advice beforeAdvice;

    /**
     * 环绕通知
     */
    private Advice aroundAdvice;

    /**
     * 后置通知
     */
    private Advice afterAdvice;

    /**
     * 后置返回通知
     */
    private Advice afterReturningAdvice;

    /**
     * 后置异常通知
     */
    private Advice afterThrowingAdvice;

    /**
     * 切点
     */
    private Pointcut pointcut;


    @Override
    public void initAspect(Class<?> clz) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            Before before = method.getAnnotation(Before.class);
            Around around = method.getAnnotation(Around.class);
            After after = method.getAnnotation(After.class);
            AfterReturning afterReturning = method.getAnnotation(AfterReturning.class);
            AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
            org.aspectj.lang.annotation.Pointcut pc = method.getAnnotation(org.aspectj.lang.annotation.Pointcut.class);
            if (null != before) {
                setBeforeAdvice(new DefaultAnnotationBeforeAdvice()
                        .setExpression(before.value()).setMethod(method)
                );
            }

            if (null != around) {
                setAroundAdvice(new DefaultAnnotationAroundAdvice()
                        .setExpression(around.value()).setMethod(method)
                );
            }
            if (null != after) {
                setAfterAdvice(new DefaultAnnotationAfterAdvice()
                        .setExpression(after.value()).setMethod(method)
                );
            }

            if (null != afterReturning) {
                setAfterReturningAdvice(new DefaultAnnotationAfterReturningAdvice()
                        .setExpression(afterReturning.value()).setMethod(method)
                );
            }

            if (null != afterThrowing) {
                setAfterThrowingAdvice(new DefaultAnnotationAfterThrowingAdvice()
                        .setExpression(afterThrowing.value()).setMethod(method)
                );
            }

            if(null != pc){
                setPointcut(new DefaultAnnotationPointcut()
                        .setExpression(pc.value()).setMethod(method)
                );
            }
        }
    }

    @Override
    public void copy(ProxyConfig config) {
        config.setAfterAdvice(this.getAfterAdvice());
        config.setAroundAdvice(this.getAroundAdvice());
        config.setAfterReturningAdvice(this.getAfterReturningAdvice());
        config.setAfterThrowingAdvice(this.getAfterThrowingAdvice());
        config.setBeforeAdvice(this.getBeforeAdvice());
    }
}

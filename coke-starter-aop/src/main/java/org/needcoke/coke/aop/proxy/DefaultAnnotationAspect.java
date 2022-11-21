package org.needcoke.coke.aop.proxy;

import lombok.Data;
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
    private BeforeAdvice beforeAdvice;

    /**
     * 环绕通知
     */
    private AroundAdvice aroundAdvice;

    /**
     * 后置通知
     */
    private AfterAdvice afterAdvice;

    /**
     * 后置返回通知
     */
    private AfterReturningAdvice afterReturningAdvice;

    /**
     * 后置异常通知
     */
    private ThrowsAdvice afterThrowingAdvice;

    /**
     * 切点
     */
    private Pointcut pointcut;


    @Override
    public void initAspect(Class<?> clz,String name) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            Before before = method.getAnnotation(Before.class);
            org.needcoke.coke.aop.annotation.Before b = method.getAnnotation(org.needcoke.coke.aop.annotation.Before.class);
            Around around = method.getAnnotation(Around.class);
            After after = method.getAnnotation(After.class);
            org.needcoke.coke.aop.annotation.After a = method.getAnnotation(org.needcoke.coke.aop.annotation.After.class);
            AfterReturning afterReturning = method.getAnnotation(AfterReturning.class);
            org.needcoke.coke.aop.annotation.AfterReturning af = method.getAnnotation(org.needcoke.coke.aop.annotation.AfterReturning.class);
            AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
            org.needcoke.coke.aop.annotation.AfterThrowing at = method.getAnnotation(org.needcoke.coke.aop.annotation.AfterThrowing.class);
            org.aspectj.lang.annotation.Pointcut pc = method.getAnnotation(org.aspectj.lang.annotation.Pointcut.class);
            org.needcoke.coke.aop.annotation.Pointcut cokePc = method.getAnnotation( org.needcoke.coke.aop.annotation.Pointcut.class);
            if (null != before) {
                setBeforeAdvice(new BeforeAdvice()
                        .setExpression(before.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != b) {
                setBeforeAdvice(new BeforeAdvice()
                        .setExpression(b.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != around) {
                setAroundAdvice(new AroundAdvice()
                        .setExpression(around.value()).setMethod(method)
                );
            }
            if (null != after) {
                setAfterAdvice(new AfterAdvice()
                        .setExpression(after.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != a) {
                setAfterAdvice(new AfterAdvice()
                        .setExpression(a.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != afterReturning) {
                setAfterReturningAdvice(new AfterReturningAdvice()
                        .setExpression(afterReturning.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != af) {
                setAfterReturningAdvice(new AfterReturningAdvice()
                        .setExpression(af.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != afterThrowing) {
                setAfterThrowingAdvice(new ThrowsAdvice()
                        .setExpression(afterThrowing.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if (null != at) {
                setAfterThrowingAdvice(new ThrowsAdvice()
                        .setExpression(at.value()).setMethod(method)
                        .setAspectName(name)
                );
            }

            if(null != pc){
                setPointcut(new DefaultAnnotationPointcut()
                        .setExpression(pc.value()).setMethod(method)
                );
            }

            if(null != cokePc){
                setPointcut(new DefaultAnnotationPointcut()
                        .setExpression(cokePc.value()).setMethod(method)
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
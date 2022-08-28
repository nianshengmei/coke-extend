package org.needcoke.aop.proxy;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.reflect.Pointcut;

/**
 * 默认的注解切面
 */
@Data
public class DefaultAnnotationAspect implements Aspect{

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



}

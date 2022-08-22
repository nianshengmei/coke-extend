package org.needcoke.aop.proxy.advice;

import java.lang.reflect.Method;

/**
 * 在返回后的通知
 *
 * @author warren
 */
public interface AfterReturningAdvice extends AfterAdvice {

    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}

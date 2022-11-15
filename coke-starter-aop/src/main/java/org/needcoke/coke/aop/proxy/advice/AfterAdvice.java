package org.needcoke.coke.aop.proxy.advice;

import java.lang.reflect.Method;

/**
 * 后置通知
 *
 * @author warren
 */
public interface AfterAdvice extends CokeAdvice {

    @Override
    default Method getMethod() {
        return null;
    }

    @Override
    default void invoke(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        returnValue = method.invoke(target,args);
    }
}

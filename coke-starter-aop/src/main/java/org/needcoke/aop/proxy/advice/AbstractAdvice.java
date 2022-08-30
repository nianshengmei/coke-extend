package org.needcoke.aop.proxy.advice;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

public abstract class AbstractAdvice implements Advice {

   public abstract Method getMethod();

    public void invoke(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        returnValue = method.invoke(target,args);
    }
}

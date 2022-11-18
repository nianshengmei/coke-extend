package org.needcoke.coke.aop.proxy;

import lombok.AllArgsConstructor;
import org.aopalliance.intercept.Joinpoint;
import org.needcoke.coke.aop.exc.ProxyException;
import org.needcoke.coke.aop.proxy.advice.AroundAdvice;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

@AllArgsConstructor
public class DefaultJoinpoint implements Joinpoint {

    private AroundAdvice aroundAdvice;

    private Object bean;

    @Override
    public Object proceed() throws Throwable {
        aroundAdvice.invoke();
        return null;
    }

    @Override
    public Object getThis() {
        return bean;
    }

    @Override
    public AccessibleObject getStaticPart() {
        throw new ProxyException("unsupported method !");
    }
}

package org.needcoke.coke.aop.proxy;

import lombok.AllArgsConstructor;
import org.aopalliance.intercept.Joinpoint;
import org.needcoke.coke.aop.exc.ProxyException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NoneJoinpoint implements Joinpoint {

    private Object bean;

    private Method method;

    private Object[] args;


    @Override
    public Object proceed() throws Throwable {
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

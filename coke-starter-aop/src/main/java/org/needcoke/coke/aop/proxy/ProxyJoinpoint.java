package org.needcoke.coke.aop.proxy;

import org.aopalliance.intercept.Joinpoint;
import org.needcoke.coke.aop.exc.ProxyException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Map;

public interface ProxyJoinpoint extends Joinpoint {

    @Override
    default Object proceed() throws Throwable {
        return null;
    }

    Object invokeMethod(Map<String, ProxyMethod> proxyMethodMap, Object proxy, Method method, Object[] args);

    @Override
    default Object getThis() {
        return this;
    }

    @Override
    default AccessibleObject getStaticPart() {
        throw new ProxyException("unsupported method !");
    }
}

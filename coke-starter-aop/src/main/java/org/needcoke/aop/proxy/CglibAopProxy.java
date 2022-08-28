package org.needcoke.aop.proxy;

/**
 * Cglib动态代理
 *
 * @author warren
 */
public class CglibAopProxy implements AopProxy {

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}

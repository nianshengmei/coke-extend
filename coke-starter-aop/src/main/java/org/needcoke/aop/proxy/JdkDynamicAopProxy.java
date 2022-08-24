package org.needcoke.aop.proxy;

import org.needcoke.aop.util.ClassUtils;

/**
 * JDK动态代理
 *
 * @author warren
 */
public class JdkDynamicAopProxy implements AopProxy {

    @Override
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}

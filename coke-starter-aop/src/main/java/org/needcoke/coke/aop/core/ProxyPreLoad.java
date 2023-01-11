package org.needcoke.coke.aop.core;

import org.aspectj.lang.annotation.Aspect;
import org.needcoke.coke.aop.proxy.MethodDeterminesProxy;
import pers.warren.ioc.core.PreLoad;

public class ProxyPreLoad implements PreLoad {

    /**
     * 提供预先加载的基础组件
     */
    @Override
    public Class<?>[] preloadBasicComponentClass() {
        return new Class[]{AopProxyFactory.class, MethodDeterminesProxy.class};
    }

    @Override
    public Class<?>[]preloadBasicComponentAnnotationClass() {
        return new Class[]{Aspect.class, org.needcoke.coke.aop.annotation.Aspect.class};
    }

    @Override
    public Class<?>[] findClasses() {
        return new Class[0];
    }
}

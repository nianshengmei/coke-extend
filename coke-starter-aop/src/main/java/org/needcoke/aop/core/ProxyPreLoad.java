package org.needcoke.aop.core;

import org.needcoke.aop.proxy.Aspect;
import pers.warren.ioc.core.PreLoad;

public class ProxyPreLoad implements PreLoad {

    /**
     * 提供预先加载的基础组件
     */
    @Override
    public Class<?>[] preloadBasicComponentClass() {
        return new Class[]{AopProxyFactory.class};
    }

    @Override
    public Class<?>[]preloadBasicComponentAnnotationClass() {
        return new Class[]{Aspect.class};
    }
}

package org.needcoke.coke.web.core;

import pers.warren.ioc.core.PreLoad;

public class WebPreLoad implements PreLoad {
    @Override
    public Class<?>[] preloadBasicComponentClass() {
        return new Class[]{HttpClientProxyFactory.class};
    }

    @Override
    public Class<?>[] preloadBasicComponentAnnotationClass() {
        return new Class[0];
    }

    @Override
    public Class<?>[] findClasses() {
        return new Class[0];
    }
}

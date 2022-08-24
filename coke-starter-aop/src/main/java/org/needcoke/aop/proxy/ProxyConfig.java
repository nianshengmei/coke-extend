package org.needcoke.aop.proxy;

import pers.warren.ioc.core.BeanDefinition;

import java.io.Serializable;

public interface ProxyConfig extends Serializable {

    static ProxyConfig createProxyConfig(BeanDefinition bdf){
        return new AspectProxyConfig(bdf);
    }
}

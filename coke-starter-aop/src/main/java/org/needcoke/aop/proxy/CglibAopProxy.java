package org.needcoke.aop.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * Cglib动态代理
 *
 * @author warren
 */
@Slf4j
public class CglibAopProxy implements AopProxy {

    private final Object bean;

    private final ProxyConfig proxyConfig;

    public CglibAopProxy(Object bean, ProxyConfig proxyConfig) {
        this.bean = bean;
        this.proxyConfig = proxyConfig;

        log.info("cglib 动态代理  {}",proxyConfig.getBeanName());
    }

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}

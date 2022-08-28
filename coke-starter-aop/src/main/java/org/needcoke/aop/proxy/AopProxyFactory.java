package org.needcoke.aop.proxy;

/**
 * AOP代理工厂
 *
 * @author warren
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(ProxyConfig config) throws AopConfigException;
}

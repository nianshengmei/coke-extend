package org.needcoke.coke.aop.core;

import org.needcoke.coke.aop.proxy.AopConfigException;
import org.needcoke.coke.aop.proxy.AopProxy;
import org.needcoke.coke.aop.proxy.ProxyConfig;

/**
 * AOP代理工厂
 *
 * @author warren
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(ProxyConfig config) throws AopConfigException;
}

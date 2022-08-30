package org.needcoke.aop.core;

import org.needcoke.aop.proxy.AopConfigException;
import org.needcoke.aop.proxy.AopProxy;
import org.needcoke.aop.proxy.ProxyConfig;

/**
 * AOP代理工厂
 *
 * @author warren
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(ProxyConfig config) throws AopConfigException;
}

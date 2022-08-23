package org.needcoke.aop.proxy;

public interface AopProxyFactory {

    AopProxy createAopProxy() throws AopConfigException;
}

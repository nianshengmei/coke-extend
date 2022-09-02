package org.needcoke.coke.aop.proxy;

/**
 * aop代理
 *
 * @author warren
 */
public interface AopProxy {

    /**
     * 获取代理对象
     */
    Object getProxy();

    /**
     * 获取代理对象
     */
    Object getProxy(ClassLoader classLoader);
}

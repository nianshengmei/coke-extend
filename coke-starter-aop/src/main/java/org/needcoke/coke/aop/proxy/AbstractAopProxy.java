package org.needcoke.coke.aop.proxy;

import org.needcoke.coke.core.Order;

public abstract class AbstractAopProxy implements AopProxy, Order {

    /**
     * 源bean的类型
     */
    protected Class<?> sourceBeanClz;

    /**
     * 源bean的名称
     */
    protected String sourceBeanName;

    /**
     * 代理相关配置
     */
    protected ProxyConfig proxyConfig;

    public AbstractAopProxy(Class<?> sourceBeanClz, String sourceBeanName, ProxyConfig proxyConfig) {
        this.sourceBeanClz = sourceBeanClz;
        this.sourceBeanName = sourceBeanName;
        this.proxyConfig = proxyConfig;
    }

    /**
     * 排序字段
     */
    protected int order;

    @Override
    public int getOrder() {
        return this.order;
    }

    public void orderIncr() {
        this.order++;
    }

}

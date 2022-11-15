package org.needcoke.coke.aop.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.needcoke.coke.aop.proxy.AopProxy;
import org.needcoke.coke.aop.proxy.ProxyMethod;
import pers.warren.ioc.core.BeanDefinition;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProxyBeanDefinition extends BeanDefinition {

    /**
     * 原始beanDefinition的名称
     */
    protected String parentName;

    /**
     * 代理集合
     */
    protected List<AopProxy> aopProxyList;

    /**
     * 被几个切面切到
     */
    protected int proxyTimes;

    /**
     * 代理方法集合
     */
    protected Map<String, ProxyMethod> proxyMethodMap;
}

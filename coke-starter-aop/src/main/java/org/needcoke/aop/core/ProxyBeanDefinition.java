package org.needcoke.aop.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.needcoke.aop.proxy.AopProxy;
import pers.warren.ioc.core.BeanDefinition;

import java.util.List;

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

}

package org.needcoke.coke.aop.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.needcoke.coke.aop.proxy.AopProxy;
import org.needcoke.coke.aop.proxy.ProxyMethod;
import pers.warren.ioc.core.BeanDefinition;

import java.lang.reflect.Method;
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
    protected AopProxy aopProxy;

    /**
     * 被几个切面切到
     */
    protected int proxyTimes;

    /**
     * 代理方法集合
     */
    protected Map<String, ProxyMethod> proxyMethodMap;

    /**
     * 所有被代理的方法
     */
    protected List<Method> proxyMethodList;
}

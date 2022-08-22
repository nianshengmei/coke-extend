package org.needcoke.aop.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import pers.warren.ioc.core.ApplicationContext;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.Container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代理容器上下文
 *
 * @author warren
 */
public class ProxyApplicationContext extends ApplicationContext {

    /**
     * key: 非代理对象bean的名称
     * <p>
     * value: 代理对象bean的名称
     */
    private final Map<String, String> proxyBeanNameMap = new HashMap<>();

    /**
     * 是否需要代理
     *
     * @param name 非代理对象的名称
     */
    public boolean isProxy(String name) {
        return this.containsBeanDefinition(name) && this.getBeanDefinition(name).isProxy();
    }

    /**
     * 获取代理bean对象
     *
     * @param name 非代理对象的名称
     */
    public <T> T getProxyBean(String name) {
        String proxyName = proxyBeanNameMap.get(name);
        if (StrUtil.isEmpty(proxyName)) {
            return null;
        }
        return Container.getContainer().getBean(proxyName);
    }

    /**
     * 获取代理bean对象
     *
     * @param clz bean的类型
     */
    public <T> T getProxyBean(Class<T> clz) {
        Container container = Container.getContainer();
        List<BeanDefinition> beanDefinitions = container.getBeanDefinitions(clz);
        List<BeanDefinition> bds = beanDefinitions.stream()
                .filter(BeanDefinition::isProxy).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(bds)) {
            return getProxyBean(bds.get(0).getName());
        }
        return null;
    }
}

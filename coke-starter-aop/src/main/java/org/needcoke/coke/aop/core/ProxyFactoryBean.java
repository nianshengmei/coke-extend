package org.needcoke.coke.aop.core;

import org.needcoke.coke.aop.proxy.AopProxy;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanFactory;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.core.FactoryBean;

import java.util.List;

public class ProxyFactoryBean implements FactoryBean {

    private final BeanDefinition beanDefinition;

    private final BeanFactory currentBeanFactory;

    private final Container container;

    public ProxyFactoryBean(BeanDefinition beanDefinition, BeanFactory currentBeanFactory) {
        this.beanDefinition = beanDefinition;
        this.currentBeanFactory = currentBeanFactory;
        this.container = Container.getContainer();
    }

    @Override
    public Object getObject() {
        if (beanDefinition instanceof ProxyBeanDefinition) {
            ProxyBeanDefinition proxyBeanDefinition = (ProxyBeanDefinition) beanDefinition;
            AopProxy aopProxy = proxyBeanDefinition.getAopProxy();
            return aopProxy.getProxy();
        }
        return null;
    }

    @Override
    public Class getType() {
        return beanDefinition.getClz();
    }

    @Override
    public Boolean isSingleton() {
        return beanDefinition.isSingleton();
    }

    @Override
    public String getName() {
        return beanDefinition.getName();
    }
}

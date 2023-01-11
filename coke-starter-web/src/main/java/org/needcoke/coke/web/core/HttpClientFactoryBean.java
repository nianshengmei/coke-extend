package org.needcoke.coke.web.core;

import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanFactory;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.core.FactoryBean;

public class HttpClientFactoryBean implements FactoryBean {

    private final Container container = Container.getContainer();

    /**
     * beanDefinition
     */
    private BeanDefinition beanDefinition;

    /**
     * 创建该FactoryBean的工厂
     */
    private BeanFactory currentBeanFactory;

    public HttpClientFactoryBean(BeanDefinition beanDefinition, BeanFactory currentBeanFactory) {
        this.beanDefinition = beanDefinition;
        this.currentBeanFactory = currentBeanFactory;
    }

    @Override
    public Object getObject() {
        HttpClientProxyFactory factory = container.getBean(HttpClientProxyFactory.class);
        return factory.getProxyHttpClient(beanDefinition.getClz());
    }

    @Override
    public Class getType() {
        return beanDefinition.getClz();
    }

    @Override
    public String getName() {
        return beanDefinition.getName();
    }

    @Override
    public Boolean isSingleton() {
        return true;
    }
}

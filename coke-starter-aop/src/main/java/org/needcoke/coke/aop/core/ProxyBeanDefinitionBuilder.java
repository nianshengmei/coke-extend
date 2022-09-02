package org.needcoke.coke.aop.core;

import org.needcoke.coke.aop.proxy.AopProxy;
import pers.warren.ioc.core.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理bean定义builder
 *
 * @author warren
 * @since coke 1.0.1
 */
public class ProxyBeanDefinitionBuilder extends BeanDefinitionBuilder {

    private ProxyBeanDefinition beanDefinition;

    private static final Map<String, Integer> proxyTimeMap = new HashMap<>();

    public ProxyBeanDefinitionBuilder() {
        this.beanDefinition = new ProxyBeanDefinition();
    }

    public static ProxyBeanDefinitionBuilder genericBeanDefinition(BeanDefinition bdf) {
        ProxyBeanDefinitionBuilder builder = new ProxyBeanDefinitionBuilder();
        if (proxyTimeMap.containsKey(bdf.getName())) {
            BeanDefinition proxyBeanDefinition = Container.getContainer().getProxyBeanDefinition(bdf.getName());
            builder.beanDefinition = (ProxyBeanDefinition) proxyBeanDefinition;
            int time = proxyTimeMap.get(bdf.getName()) + 1;
            proxyTimeMap.put(bdf.getName(), time);
            builder.beanDefinition.setProxyTimes(time);
        }else {
            builder.beanDefinition.setParentName(bdf.getName());
            builder.beanDefinition.setName(bdf.getName() + "#proxy");
            builder.beanDefinition.setClz(bdf.getClz());
            builder.beanDefinition.setScanByAnnotation(bdf.getScanByAnnotation());
            builder.beanDefinition.setScanByAnnotationClass(bdf.getScanByAnnotationClass());
            builder.beanDefinition.setBeanType(bdf.getBeanType());
            builder.beanDefinition.setAutowiredFieldInject(bdf.getAutowiredFieldInject());
            builder.beanDefinition.setPropertyValues(bdf.getPropertyValues());
            builder.beanDefinition.setRegister(bdf.getRegister());
            builder.beanDefinition.setExtendedFields(bdf.getExtendedFields());
            builder.beanDefinition.setSingleton(bdf.isSingleton());
            builder.beanDefinition.setResourceFieldInject(bdf.getResourceFieldInject());
            builder.beanDefinition.setInvokeFunction(bdf.getInvokeFunction());
            builder.beanDefinition.setInvokeSource(bdf.getInvokeSource());
            builder.beanDefinition.setFactoryBeanClass(ProxyFactoryBean.class);
            builder.beanDefinition.setBeanFactoryClass(DefaultBeanFactory.class);
            builder.beanDefinition.setValueFiledInject(bdf.getValueFiledInject());
            proxyTimeMap.put(bdf.getName(), 1);
            builder.beanDefinition.setProxyTimes(1);
        }
        return builder;
    }

    @Override
    public ProxyBeanDefinitionBuilder setFactoryBeanType(Class<?> factoryBeanClass) {
        beanDefinition.setBeanFactoryClass(factoryBeanClass);
        return this;
    }

    @Override
    public ProxyBeanDefinitionBuilder setBeanFactoryType(Class<?> beanFactoryType) {
        this.beanDefinition.setFactoryBeanClass(beanFactoryType);
        return this;
    }

    @Override
    public ProxyBeanDefinitionBuilder setRegister(BeanRegister register) {
        this.beanDefinition.setRegister(register);
        return this;
    }

    @Override
    public ProxyBeanDefinitionBuilder setScanByAnnotationClass(Class<?> annotationClass) {
        this.beanDefinition.setScanByAnnotationClass(annotationClass);
        return this;
    }

    public ProxyBeanDefinitionBuilder addAopProxy(AopProxy aopProxy) {
        if (null == this.beanDefinition.aopProxyList) {
            this.beanDefinition.aopProxyList = new ArrayList<>();
        }
        this.beanDefinition.aopProxyList.add(aopProxy);
        return this;
    }

    @Override
    public ProxyBeanDefinitionBuilder setScanByAnnotation(Annotation annotation) {
        this.beanDefinition.setScanByAnnotation(annotation);
        return this;
    }

    @Override
    public ProxyBeanDefinition build() {
        return this.beanDefinition;
    }
}

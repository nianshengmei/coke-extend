package org.needcoke.aop.core;

import pers.warren.ioc.core.*;
import pers.warren.ioc.enums.BeanType;

import java.lang.annotation.Annotation;

/**
 * 代理bean定义builder
 *
 * @author warren
 * @since coke 1.0.1
 */
public class ProxyBeanDefinitionBuilder extends BeanDefinitionBuilder {

    private ProxyBeanDefinition beanDefinition;


    public static ProxyBeanDefinitionBuilder genericBeanDefinition(BeanDefinition bdf) {
        ProxyBeanDefinitionBuilder builder = new ProxyBeanDefinitionBuilder();
        builder.beanDefinition.setParentName(bdf.getName());
        builder.beanDefinition.setName(bdf.getName()+"#proxy");
        builder.beanDefinition.setClz(bdf.getClz());
        builder.beanDefinition.setScanByAnnotation(bdf.getScanByAnnotation());
        builder.beanDefinition.setScanByAnnotationClass(bdf.getScanByAnnotationClass());

        builder.beanDefinition.setAutowiredFieldInject(bdf.getAutowiredFieldInject());
        builder.beanDefinition.setPropertyValues(bdf.getPropertyValues());
        builder.beanDefinition.setRegister(bdf.getRegister());
        builder.beanDefinition.setExtendedFields(bdf.getExtendedFields());
        builder.beanDefinition.setSingleton(bdf.isSingleton());
        builder.beanDefinition.setResourceFieldInject(bdf.getResourceFieldInject());
        builder.beanDefinition.setInvokeFunction(bdf.getInvokeFunction());
        builder.beanDefinition.setInvokeSource(bdf.getInvokeSource());
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

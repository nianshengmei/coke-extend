package org.needcoke.coke.web.core;

import org.needcoke.coke.web.annotation.HttpClient;
import pers.warren.ioc.core.*;
import pers.warren.ioc.enums.BeanType;

public class HttpClientBeanRegister extends AnnotationBeanRegister {
    @Override
    public String getName(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String beanName = null;
        boolean hasClient = importingClassMetadata.hasAnnotation(HttpClient.class);
        if (hasClient) {
            HttpClient client = (HttpClient) importingClassMetadata.getAnnotation(HttpClient.class);
            beanName = realBeanName(importingClassMetadata, registry, client.name(), client.value());
        }
        return beanName;
    }

    @Override
    public BeanDefinition initialization(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = null;
        BeanDefinitionBuilder builder = null;
        if (importingClassMetadata.hasAnnotation(HttpClient.class)) {
            String name = getName(importingClassMetadata, registry);
            builder = BeanDefinitionBuilder.genericBeanDefinition(importingClassMetadata.getSourceClass(),
                    name,
                    BeanType.COMPONENT,
                    null,
                    name
            );
            builder.setRegister(this);
            builder.setScanByAnnotationClass(HttpClient.class);
            builder.setScanByAnnotation(importingClassMetadata.getAnnotation(HttpClient.class));
            builder.setFactoryBeanType(HttpClientFactoryBean.class);
            beanDefinition = builder.build();
            registry.registerBeanDefinition(name,beanDefinition);
        }
        return beanDefinition;
    }
}

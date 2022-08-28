package org.needcoke.aop.core;

import org.aspectj.lang.annotation.Aspect;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.core.Container;

import java.lang.annotation.Annotation;

public class ProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class)) {
            Container container = Container.getContainer();
            ProxyApplicationContext proxyApplicationContext = container.getBean(ProxyApplicationContext.class);
            proxyApplicationContext.putAspect(beanDefinition.getName(), org.needcoke.aop.proxy.Aspect.createAspect());
        }
    }

    @Override
    public void postProcessAfterInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class)) {
            Container container = Container.getContainer();
            ProxyApplicationContext proxyApplicationContext = container.getBean(ProxyApplicationContext.class);
            org.needcoke.aop.proxy.Aspect aspect = proxyApplicationContext.getAspect(beanDefinition.getName());
            aspect.setAspectBean(container.getBean(beanDefinition.getName()));
        }
    }


    private <A extends Annotation> boolean containsAnnotation(Class<?> clz, Class<A> annotationClz) {
        return null != clz.getAnnotation(annotationClz);
    }
}

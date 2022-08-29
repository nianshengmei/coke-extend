package org.needcoke.aop.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.needcoke.aop.proxy.Pointcut;
import org.needcoke.aop.proxy.AspectJExpressionPointcut;
import pers.warren.ioc.core.*;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@Slf4j
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
            aspect.initAspect(beanDefinition.getClz());
        }
    }


    private <A extends Annotation> boolean containsAnnotation(Class<?> clz, Class<A> annotationClz) {
        return null != clz.getAnnotation(annotationClz);
    }

    @Override
    public void postProcessAfterBeanLoad(BeanDefinitionRegistry register) {
        ProxyApplicationContext proxyApplicationContext = register.getBean(ProxyApplicationContext.class);
        AspectJExpressionPointcut aspectJExpressionPointcut = register.getBean(AspectJExpressionPointcut.class);
        Stream<org.needcoke.aop.proxy.Aspect> aspectStream = proxyApplicationContext.getAspectStream();
        aspectStream.forEach(aspect -> {
            Pointcut pointcut = aspect.getPointcut();

        });
    }
}

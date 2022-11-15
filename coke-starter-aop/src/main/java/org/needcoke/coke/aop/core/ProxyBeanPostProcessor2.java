package org.needcoke.coke.aop.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;
import org.needcoke.coke.aop.proxy.AopProxy;
import org.needcoke.coke.aop.proxy.Pointcut;
import org.needcoke.coke.aop.proxy.ProxyConfig;
import pers.warren.ioc.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

@Slf4j
public class ProxyBeanPostProcessor2 {

    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class)) {
            Container container = Container.getContainer();
            ProxyApplicationContext proxyApplicationContext = container.getBean(ProxyApplicationContext.class);
            org.needcoke.coke.aop.proxy.Aspect aspect = org.needcoke.coke.aop.proxy.Aspect.createAspect();
            proxyApplicationContext.putAspect(beanDefinition.getName(), aspect);
            aspect.setAspectBean(container.getBean(beanDefinition.getName()));
            aspect.initAspect(beanDefinition.getClz());
            Collection<BeanWrapper> beanWrappers = container.getBeanWrappers();
            AopProxyFactory proxyFactory = container.getBean(AopProxyFactory.class);
            Pointcut pointcut = aspect.getPointcut();
            PointcutExpression pointcutExpression = pointcut.getPointcutExpression();
            for (BeanWrapper wrapper : beanWrappers) {
                Method[] declaredMethods = wrapper.getClz().getDeclaredMethods();
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.setBeanName(wrapper.getName());
                boolean flag = false;
                for (Method declaredMethod : declaredMethods) {
                    ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(declaredMethod);
                    if (shadowMatch.alwaysMatches()) {
                        proxyConfig.addMethod(declaredMethod);
                        wrapper.getBeanDefinition().setProxy(true);
                        flag = true;
                    }
                }
                if (flag) {
                    pointcut.getProxyConfigList().add(proxyConfig);
                    aspect.copy(proxyConfig);
                    AopProxy aopProxy = proxyFactory.createAopProxy(proxyConfig);
                    ProxyBeanDefinitionBuilder builder = ProxyBeanDefinitionBuilder.genericBeanDefinition(wrapper.getBeanDefinition());
                    builder.addAopProxy(aopProxy);
                    container.addBeanDefinition(builder.build());
                }
            }
        }
    }


    private <A extends Annotation> boolean containsAnnotation(Class<?> clz, Class<A> annotationClz) {
        return null != clz.getAnnotation(annotationClz);
    }

}

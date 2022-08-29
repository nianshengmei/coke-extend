package org.needcoke.aop.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;
import org.needcoke.aop.proxy.Pointcut;
import org.needcoke.aop.proxy.AspectJExpressionPointcut;
import org.needcoke.aop.proxy.ProxyConfig;
import pers.warren.ioc.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
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
        Collection<BeanWrapper> beanWrappers = register.getBeans();


        aspectStream.forEach(aspect -> {
            Pointcut pointcut = aspect.getPointcut();
            PointcutExpression pointcutExpression = pointcut.getPointcutExpression();
            for (BeanWrapper wrapper : beanWrappers) {
                Method[] declaredMethods = wrapper.getBean().getClass().getDeclaredMethods();
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.setBeanName(wrapper.getName());
                boolean flag = false;
                for (Method declaredMethod : declaredMethods) {
                    ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(declaredMethod);
                    if (shadowMatch.alwaysMatches() && !Modifier.isStatic(declaredMethod.getModifiers())
                            && Modifier.isPublic(declaredMethod.getModifiers())
                    ) {
                        proxyConfig.addMethod(declaredMethod);
                        flag = true;
                    }
                }
                if (flag) {
                    pointcut.getProxyConfigList().add(proxyConfig);
                }
            }
        });
    }
}

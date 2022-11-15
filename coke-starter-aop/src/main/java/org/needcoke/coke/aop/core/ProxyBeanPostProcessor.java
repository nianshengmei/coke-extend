package org.needcoke.coke.aop.core;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;
import org.needcoke.coke.aop.proxy.Pointcut;
import org.needcoke.coke.aop.proxy.ProxyMethod;
import pers.warren.ioc.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class ProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        Container container = Container.getContainer();
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class)) {
            org.needcoke.coke.aop.proxy.Aspect aspect = org.needcoke.coke.aop.proxy.Aspect.createAspect();
            aspect.setAspectBean(Container.getContainer().getBean(beanDefinition.getName()));
            aspect.initAspect(beanDefinition.getClz());
            Pointcut pointcut = aspect.getPointcut(); //切点
            PointcutExpression pointcutExpression = pointcut.getPointcutExpression(); //切点表达式
            Collection<BeanWrapper> beanWrappers = container.getBeanWrappers();
            for (BeanWrapper wrapper : beanWrappers) {
                Method[] declaredMethods = wrapper.getClz().getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(declaredMethod);
                    if (shadowMatch.alwaysMatches()) {

                        if (!container.containsProxyBeanDefinition(wrapper.getName())) {
                            ProxyBeanDefinitionBuilder proxyBeanDefinitionBuilder = ProxyBeanDefinitionBuilder.genericBeanDefinition(wrapper.getBeanDefinition());
                            container.addBeanDefinition(proxyBeanDefinitionBuilder.build());
                        }
                        ProxyBeanDefinition proxyBeanDefinition = (ProxyBeanDefinition) container.getProxyBeanDefinition(wrapper.getName());
                        if (null == proxyBeanDefinition.proxyMethodMap) {
                            proxyBeanDefinition.proxyMethodMap = new HashMap<>();
                        }
                        if (!proxyBeanDefinition.proxyMethodMap.containsKey(declaredMethod.toGenericString())) {
                            proxyBeanDefinition.proxyMethodMap.put(declaredMethod.toGenericString(), new ProxyMethod().setBeanName(wrapper.getName()).setMethod(declaredMethod));
                        }
                    }

                }

            }
        }
    }

    private <A extends Annotation> boolean containsAnnotation(Class<?> clz, Class<A> annotationClz) {
        return null != clz.getAnnotation(annotationClz);
    }


    private String getMethodName(Method method) {
        return method.toGenericString();
    }
}

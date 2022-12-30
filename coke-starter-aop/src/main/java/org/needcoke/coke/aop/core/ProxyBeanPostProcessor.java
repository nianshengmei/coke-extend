package org.needcoke.coke.aop.core;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;
import org.needcoke.coke.aop.annotation.Aspect;
import org.needcoke.coke.aop.proxy.AopProxy;
import org.needcoke.coke.aop.proxy.Pointcut;
import org.needcoke.coke.aop.proxy.ProxyMethod;
import org.needcoke.coke.aop.util.MethodUtil;
import pers.warren.ioc.core.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ProxyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        Container container = Container.getContainer();
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class) ||containsAnnotation(beanDefinition.getClz(), org.aspectj.lang.annotation.Aspect.class)) {
            org.needcoke.coke.aop.proxy.Aspect aspect = org.needcoke.coke.aop.proxy.Aspect.createAspect();
            aspect.setAspectBean(Container.getContainer().getBean(beanDefinition.getName()));
            aspect.initAspect(beanDefinition.getClz(),beanDefinition.getName());
            Pointcut pointcut = aspect.getPointcut(); //切点
            PointcutExpression pointcutExpression = pointcut.getPointcutExpression(); //切点表达式
            Collection<BeanWrapper> beanWrappers = container.getBeanWrappers();
            for (BeanWrapper wrapper : beanWrappers) {
                Method[] declaredMethods = wrapper.getClz().getDeclaredMethods();
                ProxyBeanDefinition proxyBeanDefinition = (ProxyBeanDefinition) container.getProxyBeanDefinition(wrapper.getName());
                for (Method declaredMethod : declaredMethods) {
                    ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(declaredMethod);
                    if (shadowMatch.alwaysMatches()) {
                        if (!container.containsProxyBeanDefinition(wrapper.getName())) {
                            ProxyBeanDefinitionBuilder proxyBeanDefinitionBuilder = ProxyBeanDefinitionBuilder.genericBeanDefinition(wrapper.getBeanDefinition());
                            proxyBeanDefinition = proxyBeanDefinitionBuilder.build();
                            container.addBeanDefinition(proxyBeanDefinition);
                        }
                        if (null == proxyBeanDefinition.proxyMethodMap) {
                            proxyBeanDefinition.proxyMethodMap = new HashMap<>();
                        }
                        if (!proxyBeanDefinition.proxyMethodMap.containsKey(MethodUtil.getMethodName(declaredMethod))) {
                            proxyBeanDefinition.proxyMethodMap.put(getMethodName(declaredMethod), new ProxyMethod().setBeanName(wrapper.getName()).setMethod(declaredMethod));
                        }
                        ProxyMethod proxyMethod = proxyBeanDefinition.proxyMethodMap.get(getMethodName(declaredMethod));
                        initProxyMethod(aspect, proxyMethod);
                        if(null == proxyBeanDefinition.getProxyMethodList()){
                            proxyBeanDefinition.setProxyMethodList(new ArrayList<>());
                        }
                        proxyBeanDefinition.getProxyMethodList().add(declaredMethod);

                    }

                }
                if(null != proxyBeanDefinition && null == proxyBeanDefinition.getAopProxy()) {
                    AopProxy aopProxy = Container.getContainer().getBean(AopProxyFactory.class).createAopProxy(proxyBeanDefinition.getName());
                    proxyBeanDefinition.setAopProxy(aopProxy);
                    ProxyBeanDefinition pbd= (ProxyBeanDefinition) container.getProxyBeanDefinition(wrapper.getName());
                }
            }


        }
    }

    private <A extends Annotation> boolean containsAnnotation(Class<?> clz, Class<A> annotationClz) {
        return null != clz.getAnnotation(annotationClz);
    }


    private String getMethodName(Method method) {
        return MethodUtil.getMethodName(method);
    }


    private void initProxyMethod(org.needcoke.coke.aop.proxy.Aspect aspect, ProxyMethod proxyMethod) {
        /* 前置通知 */
        if (null != aspect.getBeforeAdvice()) {
            if (null == proxyMethod.getBeforeAdvices()) {
                proxyMethod.setBeforeAdvices(new ArrayList<>());
            }
            proxyMethod.getBeforeAdvices().add(aspect.getBeforeAdvice());
        }

        /* 环绕通知 */
        if (null != aspect.getAroundAdvice()) {
            if (null == proxyMethod.getAroundAdvices()) {
                proxyMethod.setAroundAdvices(new ArrayList<>());
            }
            proxyMethod.getAroundAdvices().add(aspect.getAroundAdvice());
        }

        /* 后置通知 */
        if (null != aspect.getAfterAdvice()) {
            if (null == proxyMethod.getAfterAdvices()) {
                proxyMethod.setAfterAdvices(new ArrayList<>());
            }
            proxyMethod.getAfterAdvices().add(aspect.getAfterAdvice());
        }


        if (null != aspect.getAfterReturningAdvice()) {
            if (null == proxyMethod.getAfterReturningAdvices()) {
                proxyMethod.setAfterReturningAdvices(new ArrayList<>());
            }
            proxyMethod.getAfterReturningAdvices().add(aspect.getAfterReturningAdvice());
        }

        if (null != aspect.getAfterThrowingAdvice()) {
            if (null == proxyMethod.getThrowsAdvices()) {
                proxyMethod.setThrowsAdvices(new ArrayList<>());
            }
            proxyMethod.getThrowsAdvices().add(aspect.getAfterThrowingAdvice());
        }
    }


}

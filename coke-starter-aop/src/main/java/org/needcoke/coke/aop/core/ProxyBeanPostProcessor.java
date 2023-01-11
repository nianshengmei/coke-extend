package org.needcoke.coke.aop.core;

import cn.hutool.core.collection.CollUtil;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;
import org.needcoke.coke.aop.annotation.Aspect;
import org.needcoke.coke.aop.proxy.*;
import org.needcoke.coke.aop.proxy.advice.AfterAdvice;
import org.needcoke.coke.aop.proxy.advice.AfterReturningAdvice;
import org.needcoke.coke.aop.proxy.advice.BeforeAdvice;
import org.needcoke.coke.aop.proxy.advice.ThrowsAdvice;
import org.needcoke.coke.aop.util.MethodUtil;
import pers.warren.ioc.core.*;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        Container container = Container.getContainer();
        if (containsAnnotation(beanDefinition.getClz(), Aspect.class) || containsAnnotation(beanDefinition.getClz(), org.aspectj.lang.annotation.Aspect.class)) {
            org.needcoke.coke.aop.proxy.Aspect aspect = org.needcoke.coke.aop.proxy.Aspect.createAspect();
            aspect.setAspectBean(Container.getContainer().getBean(beanDefinition.getName()));
            aspect.initAspect(beanDefinition.getClz(), beanDefinition.getName());
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
                        if (null == proxyBeanDefinition.getProxyMethodList()) {
                            proxyBeanDefinition.setProxyMethodList(new ArrayList<>());
                        }
                        proxyBeanDefinition.getProxyMethodList().add(declaredMethod);

                    }

                }
                if (null != proxyBeanDefinition && null == proxyBeanDefinition.getAopProxy()) {
                    AopProxy aopProxy = Container.getContainer().getBean(AopProxyFactory.class).createAopProxy(proxyBeanDefinition.getName());
                    proxyBeanDefinition.setAopProxy(aopProxy);
                    ProxyBeanDefinition pbd = (ProxyBeanDefinition) container.getProxyBeanDefinition(wrapper.getName());
                }
            }


        }

        methodDeterminesProxy(beanDefinition);
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


    public void methodDeterminesProxy(BeanDefinition beanDefinition) {
        //方法决定代理类
        MethodDeterminesProxy determinesProxy = Container.getContainer().getBean(MethodDeterminesProxy.class);
        if (null == determinesProxy) {
            return;
        }
        for (MethodStrategy strategy : determinesProxy.methodAnnotationClasses()) {
            Method[] declaredMethods = beanDefinition.getClz().getDeclaredMethods();
            List<Method> proxyMethodList = Arrays.stream(declaredMethods).filter(method -> ReflectUtil.containsAnnotation(method, strategy.getMethodAnnotation()))
                    .collect(Collectors.toList());
            if (proxyMethodList.size() > 0) {   //当前bean需要被代理
                Container container = Container.getContainer();
                boolean b = container.containsProxyBean(beanDefinition.getName());
                ProxyBeanDefinition pbd;
                if (!b) {        //该bean未被代理过
                    pbd = ProxyBeanDefinitionBuilder.genericBeanDefinition(beanDefinition).build();
                    pbd.setAopProxy(container.getProxyBean(AopProxyFactory.class).createAopProxy(beanDefinition.getName()));
                } else {
                    pbd = (ProxyBeanDefinition) Container.getContainer().getProxyBeanDefinition(beanDefinition.getName());
                }
                if (CollUtil.isEmpty(pbd.getProxyMethodList())) {
                    pbd.setProxyMethodList(new ArrayList<>());
                }
                BeanDefinition aspectBdf = container.getBeanDefinition(strategy.getAspectName());
                if (null == pbd.getProxyMethodMap()) {
                    pbd.setProxyMethodMap(new HashMap<>());
                }
                for (Method declaredMethod : declaredMethods) {
                    ProxyMethod proxyMethod = new ProxyMethod();
                    proxyMethod.setBeanName(beanDefinition.getName())
                            .setMethod(declaredMethod);
                    pbd.getProxyMethodList().add(declaredMethod);
                    switch (strategy.getType()) {
                        case AFTER:
                            if (null == proxyMethod.getAfterAdvices()) {
                                proxyMethod.setAfterAdvices(new ArrayList<>());
                            }
                            List<Method> afterMethods = Arrays.stream(aspectBdf.getClz().getDeclaredMethods()).filter(method -> method.getName().equals("after")).collect(Collectors.toList());
                            if (CollUtil.size(afterMethods) == 1) {
                                throw new RuntimeException("采用方法决定代理(MethodDeterminesProxy)的切面，定义After方法必须有唯一一个对应的名为after的方法用作after通知!");
                            }
                            AfterAdvice afterAdvice = new AfterAdvice().setAspectName(strategy.getAspectName()).setMethod(afterMethods.get(0));
                            proxyMethod.getAfterAdvices().add(afterAdvice);
                            break;
                        case BEFORE:
                            if (null == proxyMethod.getBeforeAdvices()) {
                                proxyMethod.setBeforeAdvices(new ArrayList<>());
                            }
                            List<Method> beforeMethods = Arrays.stream(aspectBdf.getClz().getDeclaredMethods()).filter(method -> method.getName().equals("before")).collect(Collectors.toList());
                            if (CollUtil.size(beforeMethods) == 1) {
                                throw new RuntimeException("采用方法决定代理(MethodDeterminesProxy)的切面，定义before方法必须有唯一一个对应的名为before的方法用作before通知!");
                            }
                            BeforeAdvice beforeAdvice = new BeforeAdvice().setAspectName(strategy.getAspectName()).setMethod(beforeMethods.get(0));
                            proxyMethod.getBeforeAdvices().add(beforeAdvice);
                            break;
                        case AFTER_EXCEPTION:
                            if (null == proxyMethod.getThrowsAdvices()) {
                                proxyMethod.setThrowsAdvices(new ArrayList<>());
                            }
                            List<Method> throwsMethods = Arrays.stream(aspectBdf.getClz().getDeclaredMethods()).filter(method -> method.getName().equals("throws")).collect(Collectors.toList());
                            if (CollUtil.size(throwsMethods) == 1) {
                                throw new RuntimeException("采用方法决定代理(MethodDeterminesProxy)的切面，定义after exception方法必须有唯一一个对应的名为throws的方法用作after exception通知!");
                            }
                            ThrowsAdvice throwsAdvice = new ThrowsAdvice().setAspectName(strategy.getAspectName()).setMethod(throwsMethods.get(0));
                            proxyMethod.getThrowsAdvices().add(throwsAdvice);
                            break;

                        case AFTER_RETURNING:
                            if (null == proxyMethod.getAfterReturningAdvices()) {
                                proxyMethod.setAfterReturningAdvices(new ArrayList<>());
                            }
                            List<Method> afterReturningMethods = Arrays.stream(aspectBdf.getClz().getDeclaredMethods()).filter(method -> method.getName().equals("afterReturning")).collect(Collectors.toList());
                            if (CollUtil.size(afterReturningMethods) == 1) {
                                throw new RuntimeException("采用方法决定代理(MethodDeterminesProxy)的切面，定义after returning方法必须有唯一一个对应的名为afterReturning的方法用作after returning通知!");
                            }
                            AfterReturningAdvice afterReturningAdvice = new AfterReturningAdvice().setAspectName(strategy.getAspectName()).setMethod(afterReturningMethods.get(0));
                            proxyMethod.getAfterReturningAdvices().add(afterReturningAdvice);
                            break;

                        default:
                            throw new RuntimeException("what type advice ? coke not support !");
                    }
                    pbd.getProxyMethodMap().put(MethodUtil.getMethodName(declaredMethod), proxyMethod);
                    if (null == pbd.getAopProxy()) {
                        AopProxy aopProxy = Container.getContainer().getBean(AopProxyFactory.class).createAopProxy(pbd.getName());
                        pbd.setAopProxy(aopProxy);
                    }

                }
            }
        }
    }
}

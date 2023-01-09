package org.needcoke.coke.kafka.core;

import cn.hutool.core.collection.CollUtil;
import org.needcoke.coke.aop.core.AopProxyFactory;
import org.needcoke.coke.aop.core.ProxyBeanDefinition;
import org.needcoke.coke.aop.core.ProxyBeanDefinitionBuilder;
import org.needcoke.coke.aop.proxy.MethodDeterminesProxy;
import org.needcoke.coke.aop.proxy.MethodStrategy;
import org.needcoke.coke.aop.proxy.ProxyMethod;
import org.needcoke.coke.aop.proxy.advice.AfterAdvice;
import org.needcoke.coke.aop.proxy.advice.AfterReturningAdvice;
import org.needcoke.coke.aop.proxy.advice.BeforeAdvice;
import org.needcoke.coke.aop.proxy.advice.ThrowsAdvice;
import org.needcoke.coke.aop.util.MethodUtil;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KafkaBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        //方法决定代理类
        MethodDeterminesProxy determinesProxy = Container.getContainer().getBean(MethodDeterminesProxy.class);
        for (MethodStrategy strategy : determinesProxy.methodAnnotationClasses()) {
            Method[] declaredMethods = beanDefinition.getClz().getDeclaredMethods();
            List<Method> proxyMethodList = Arrays.stream(declaredMethods).filter(method -> ReflectUtil.containsAnnotation(method,strategy.getMethodAnnotation()))
                    .collect(Collectors.toList());
            if(proxyMethodList.size() > 0){   //当前bean需要被代理
                Container container = Container.getContainer();
                boolean b = container.containsProxyBean(beanDefinition.getName());
                ProxyBeanDefinition pbd;
                if(!b){        //该bean未被代理过
                    pbd = ProxyBeanDefinitionBuilder.genericBeanDefinition(beanDefinition).build();
                    pbd.setAopProxy(container.getProxyBean(AopProxyFactory.class).createAopProxy(beanDefinition.getName()));
                } else {
                    pbd = (ProxyBeanDefinition) Container.getContainer().getProxyBeanDefinition(beanDefinition.getName());
                }
                BeanDefinition aspectBdf = container.getBeanDefinition(strategy.getAspectName());
                if (null == pbd.getProxyMethodMap()) {
                    pbd.setProxyMethodMap(new HashMap<>());
                }
                for (Method declaredMethod : declaredMethods) {
                    ProxyMethod proxyMethod = new ProxyMethod();
                    proxyMethod.setBeanName(beanDefinition.getName())
                                    .setMethod(declaredMethod);
                    switch (strategy.getType()) {
                        case AFTER:
                            if (null == proxyMethod.getAfterAdvices()) {
                                proxyMethod.setAfterAdvices(new ArrayList<>());
                            }
                            List<Method> afterMethods = Arrays.stream(aspectBdf.getClz().getDeclaredMethods()).filter(method -> method.getName().equals("after")).collect(Collectors.toList());
                            if(CollUtil.size(afterMethods) == 1){
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
                            if(CollUtil.size(beforeMethods) == 1){
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
                            if(CollUtil.size(throwsMethods) == 1){
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
                            if(CollUtil.size(afterReturningMethods) == 1){
                                throw new RuntimeException("采用方法决定代理(MethodDeterminesProxy)的切面，定义after returning方法必须有唯一一个对应的名为afterReturning的方法用作after returning通知!");
                            }
                            AfterReturningAdvice afterReturningAdvice = new AfterReturningAdvice().setAspectName(strategy.getAspectName()).setMethod(afterReturningMethods.get(0));
                            proxyMethod.getAfterReturningAdvices().add(afterReturningAdvice);
                            break;

                        default:
                            throw new RuntimeException("what type advice ? coke not support !");
                    }
                    pbd.getProxyMethodMap().put(MethodUtil.getMethodName(declaredMethod),proxyMethod);
                    //TODO 哈哈哈哈哈哈
                }

            }
        }
    }
}

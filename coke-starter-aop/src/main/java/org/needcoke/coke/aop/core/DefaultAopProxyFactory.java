package org.needcoke.coke.aop.core;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.needcoke.coke.aop.proxy.*;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.Container;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultAopProxyFactory implements AopProxyFactory {

    private final ProxyApplicationContext applicationContext;

    @Override
    public AopProxy createAopProxy(String beanName) throws AopConfigException {
        BeanDefinition beanDefinition = applicationContext.getBeanDefinition(beanName);
        if (isJdkProxy(beanDefinition.getClz(), beanName)) {
            return new JdkDynamicAopProxy(beanDefinition.getClz(), beanName);
        } else {
            return new CglibAopProxy(beanDefinition.getClz(), beanName);
        }
    }

    private boolean isJdkProxy(Class<?> clz, String beanName) {
        Class<?>[] interfaces = clz.getInterfaces();
        if (interfaces.length != 1) {
            return false;
        }
        Class<?> ife = interfaces[0];
        Method[] declaredMethods = ife.getDeclaredMethods();
        ProxyBeanDefinition proxyBeanDefinition = (ProxyBeanDefinition) applicationContext.getBeanDefinition(beanName);
        Collection<Method> methodCollection = proxyBeanDefinition.proxyMethodList;
        boolean ret = false;
        if (CollUtil.isEmpty(methodCollection)) {
            return ret;
        }
        for (Method declaredMethod : declaredMethods) {
            ret = contains(declaredMethod, methodCollection);
        }
        return ret;
    }

    private boolean contains(Method method, Collection<Method> methods) {
        MethodInfo methodInfo = MethodInfo.info(method);
        for (Method m : methods) {
            MethodInfo info = MethodInfo.info(m);
            if (methodInfo.equals(info)) {
                return true;
            }
        }
        return false;
    }

    @Data
    @Accessors(chain = true)
    public static class MethodInfo {
        private String name;

        private Class<?>[] parameterTypes;

        public boolean equals(MethodInfo info) {
            if (!name.equals(info.name)) {
                return false;
            }
            for (Class<?> parameterType : parameterTypes) {
                if (!info.contains(parameterType)) {
                    return false;
                }
            }
            return true;
        }

        public static MethodInfo info(Method method) {
            return new MethodInfo().setName(method.getName()).setParameterTypes(method.getParameterTypes());
        }

        private boolean contains(Class<?> parameterType) {
            for (Class<?> type : parameterTypes) {
                if (parameterType.getTypeName().equals(type.getTypeName())) {
                    return true;
                }
            }
            return false;
        }
    }


}
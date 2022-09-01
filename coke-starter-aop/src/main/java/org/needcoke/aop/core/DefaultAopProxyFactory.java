package org.needcoke.aop.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.needcoke.aop.proxy.*;

import java.lang.reflect.Method;
import java.util.Collection;

@RequiredArgsConstructor
public class DefaultAopProxyFactory implements AopProxyFactory {

    private final ProxyApplicationContext applicationContext;

    @Override
    public AopProxy createAopProxy(ProxyConfig config) throws AopConfigException {
        String beanName = config.getBeanName();
        Object bean = applicationContext.getBean(beanName);
        Object proxyBean = applicationContext.getProxyBean(beanName);
        if (null == proxyBean) {
            proxyBean = bean;
        }
        if (isJdkProxy(bean.getClass(), config)) {
            return new JdkDynamicAopProxy(proxyBean, config);
        } else {
            return new CglibAopProxy(proxyBean, config);
        }
    }

    private boolean isJdkProxy(Class<?> clz, ProxyConfig config) {
        Class<?>[] interfaces = clz.getInterfaces();
        if (interfaces.length != 1) {
            return false;
        }
        Class<?> ife = interfaces[0];
        Method[] declaredMethods = ife.getDeclaredMethods();
        Collection<Method> methodCollection = config.getMethodCollection();
        boolean ret = false;
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

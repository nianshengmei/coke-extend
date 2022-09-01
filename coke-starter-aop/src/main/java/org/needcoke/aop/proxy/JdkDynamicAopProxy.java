package org.needcoke.aop.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.needcoke.aop.proxy.advice.AbstractAdvice;
import org.needcoke.aop.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 *
 * @author warren
 */
@Slf4j
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private Object bean;

    private ProxyConfig proxyConfig;

    public JdkDynamicAopProxy(Object bean, ProxyConfig proxyConfig) {
        this.bean = bean;
        this.proxyConfig = proxyConfig;
        log.info("jdk 动态代理  {}", proxyConfig.getBeanName());
    }

    @Override
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, bean.getClass().getInterfaces()
                , this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean flag = proxyConfig.contains(method);
        adviceInvoke(proxyConfig.getBeforeAdvice(), flag);
        Object invoke = method.invoke(this.bean, args);

        return invoke;
    }

    private Object adviceInvoke(Advice advice,boolean flag) throws Throwable {
        Object r = null;
        if(null != advice && flag){
            AbstractAdvice abstractAdvice = (AbstractAdvice) advice;
            abstractAdvice.invoke(r,abstractAdvice.getMethod(),new Object[0], bean);
        }
        return r;
    }
}

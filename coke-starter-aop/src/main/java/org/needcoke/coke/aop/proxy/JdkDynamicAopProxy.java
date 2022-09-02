package org.needcoke.coke.aop.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.needcoke.coke.aop.proxy.advice.AbstractAdvice;
import org.needcoke.coke.aop.util.ClassUtils;
import pers.warren.ioc.core.Container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 *
 * @author warren
 */
@Slf4j
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {


    public JdkDynamicAopProxy(Class<?> sourceBeanClz, String sourceBeanName, ProxyConfig proxyConfig) {
        super(sourceBeanClz, sourceBeanName, proxyConfig);
        log.info("jdk 动态代理  {}",proxyConfig.getBeanName());
    }

    @Override
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, sourceBeanClz.getInterfaces()
                , this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Container container = Container.getContainer();
        boolean flag = proxyConfig.contains(method);
        adviceInvoke(proxyConfig.getBeforeAdvice(), flag);

        Object invoke = method.invoke(getBean(), args);

        return invoke;
    }

    private Object adviceInvoke(Advice advice,boolean flag) throws Throwable {
        Object r = null;
        if(null != advice && flag){
            AbstractAdvice abstractAdvice = (AbstractAdvice) advice;
            abstractAdvice.invoke(r,abstractAdvice.getMethod(),new Object[0], getBean());
        }
        return r;
    }

    /**
     * 容器中获取bean,有代理bean存在则获取代理bean，没有则获取非代理bean
     */
    public Object getBean(){
        return null;
    }
}

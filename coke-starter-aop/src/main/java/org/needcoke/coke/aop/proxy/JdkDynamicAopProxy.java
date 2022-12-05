package org.needcoke.coke.aop.proxy;

import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.aop.core.ProxyBeanDefinition;
import org.needcoke.coke.aop.util.ClassUtils;
import org.needcoke.coke.aop.util.MethodUtil;
import org.needcoke.coke.http.ThrowsNotifyObject;
import pers.warren.ioc.core.Container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * JDK动态代理
 *
 * @author warren
 */
@Slf4j
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(Class<?> sourceBeanClz, String sourceBeanName) {
        super(sourceBeanClz, sourceBeanName);
        log.info("jdk 动态代理  {}", sourceBeanName);
    }

    @Override
    public Object getProxy() {
        return getProxy(sourceBeanClz.getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, sourceBeanClz.getInterfaces()
                , this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Container container = Container.getContainer();
        ProxyBeanDefinition proxyBeanDefinition = (ProxyBeanDefinition) container.getProxyBeanDefinition(sourceBeanName);
        Map<String, ProxyMethod> proxyMethodMap = proxyBeanDefinition.getProxyMethodMap();
        return invokeNoAround(proxyMethodMap, proxy, method, args);
    }

    public Object invokeNoAround(Map<String, ProxyMethod> proxyMethodMap, Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        Throwable exp = null;
        try {
            ret = withExceptionInvoke(proxyMethodMap, proxy, method, args);
            return ret;
        } catch (Throwable throwable) {
            exp = throwable;
            if (null == proxyMethodMap) {
                throw throwable;
            }
            ProxyMethod proxyMethod = proxyMethodMap.get(MethodUtil.getMethodName(method));
            ThrowsNotifyObject throwsNotifyObject = new ThrowsNotifyObject(throwable, invokeThrowsAdvice(proxyMethod, method, args, throwable));
            return ClassUtils.createDynamicClzObject(method.getReturnType(), throwsNotifyObject);
        } finally {
            if (null != proxyMethodMap) {
                ProxyMethod proxyMethod = proxyMethodMap.get(MethodUtil.getMethodName(method));
                if (null == exp) {
                    invokeAfterReturningAdvice(proxyMethod, method, args, ret);
                }
                invokeAfterAdvice(proxyMethod, method, args, ret, exp);
            }
        }
    }

    public Object withExceptionInvoke(Map<String, ProxyMethod> proxyMethodMap, Object proxy, Method method, Object[] args) throws Throwable {
        Container container = Container.getContainer();
        Object target = container.getSimpleBean(sourceBeanName);
        if (null == proxyMethodMap) {
            return method.invoke(target, args);
        }
        ProxyMethod proxyMethod = proxyMethodMap.get(MethodUtil.getMethodName(method));
        invokeBeforeAdvice(proxyMethod, method, args);
        return method.invoke(target, args);
    }


}

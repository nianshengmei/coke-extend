package org.needcoke.coke.aop.proxy;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.needcoke.coke.aop.core.DefaultAopProxyFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 代理配置
 *
 * @author warren
 */
@Data
public class ProxyConfig implements Serializable {

    /**
     * bean名称
     */
    private String beanName;

    /**
     * 前置通知
     */
    private Advice beforeAdvice;

    /**
     * 环绕通知
     */
    private Advice aroundAdvice;

    /**
     * 后置通知
     */
    private Advice afterAdvice;

    /**
     * 后置返回通知
     */
    private Advice afterReturningAdvice;

    /**
     * 后置异常通知
     */
    private Advice afterThrowingAdvice;



    /**
     * 被切到的方法集合
     */
    private Collection<Method> methodCollection = new ArrayList<>();

    /**
     * 获取bean名称
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 设置bean名称
     */
    public ProxyConfig setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    /**
     * 添加方法
     */
    public void addMethod(Method method) {
        this.methodCollection.add(method);
    }

    public boolean contains(Method method) {
        DefaultAopProxyFactory.MethodInfo methodInfo = DefaultAopProxyFactory.MethodInfo.info(method);
        for (Method m : methodCollection) {
            DefaultAopProxyFactory.MethodInfo info = DefaultAopProxyFactory.MethodInfo.info(m);
            if (methodInfo.equals(info)) {
                return true;
            }
        }
        return false;
    }
}

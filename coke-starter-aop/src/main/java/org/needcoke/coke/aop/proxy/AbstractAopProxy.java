package org.needcoke.coke.aop.proxy;

import cn.hutool.core.collection.CollUtil;
import org.needcoke.coke.aop.proxy.advice.AfterAdvice;
import org.needcoke.coke.aop.proxy.advice.AfterReturningAdvice;
import org.needcoke.coke.aop.proxy.advice.BeforeAdvice;
import org.needcoke.coke.aop.proxy.advice.ThrowsAdvice;
import org.needcoke.coke.core.Order;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractAopProxy implements AopProxy, Order {

    /**
     * 源bean的类型
     */
    protected Class<?> sourceBeanClz;

    /**
     * 源bean的名称
     */
    protected String sourceBeanName;

    public AbstractAopProxy(Class<?> sourceBeanClz, String sourceBeanName) {
        this.sourceBeanClz = sourceBeanClz;
        this.sourceBeanName = sourceBeanName;
    }

    /**
     * 排序字段
     */
    protected int order;

    @Override
    public int getOrder() {
        return this.order;
    }

    public void orderIncr() {
        this.order++;
    }

    public void invokeBeforeAdvice(ProxyMethod proxyMethod, Method method, Object[] args) throws Throwable {
        List<BeforeAdvice> beforeAdvices = proxyMethod.getBeforeAdvices();
        if(CollUtil.isNotEmpty(beforeAdvices)) {
            for (BeforeAdvice beforeAdvice : beforeAdvices) {
                beforeAdvice.invoke(method, args);
            }
        }
    }

    public void invokeAfterAdvice(ProxyMethod proxyMethod, Method method, Object[] args, Object returnValue, Throwable throwable) throws Throwable {
        List<AfterAdvice> afterAdvices = proxyMethod.getAfterAdvices();
        if(CollUtil.isNotEmpty(afterAdvices)) {
            for (AfterAdvice afterAdvice : afterAdvices) {
                afterAdvice.invoke(method, args, returnValue, throwable);
            }
        }
    }

    public void invokeAfterReturningAdvice(ProxyMethod proxyMethod, Method method, Object[] args, Object returnValue) throws Throwable {
        List<AfterReturningAdvice> afterReturningAdvices = proxyMethod.getAfterReturningAdvices();
        if(CollUtil.isNotEmpty(afterReturningAdvices)) {
            for (AfterReturningAdvice afterReturningAdvice : afterReturningAdvices) {
                afterReturningAdvice.invoke(method, args, returnValue);
            }
        }
    }


    public Object invokeThrowsAdvice(ProxyMethod proxyMethod, Method method, Object[] args, Throwable throwable) throws Throwable {
        Object ret = null;
        List<ThrowsAdvice> throwsAdvices = proxyMethod.getThrowsAdvices();
        if(CollUtil.isNotEmpty(throwsAdvices)) {
            for (ThrowsAdvice throwsAdvice : throwsAdvices) {
                ret = throwsAdvice.invoke(method, args, throwable);
            }
        }
        return ret;
    }

}

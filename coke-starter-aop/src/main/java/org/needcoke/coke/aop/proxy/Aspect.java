package org.needcoke.coke.aop.proxy;

import org.aopalliance.aop.Advice;
import org.needcoke.coke.aop.proxy.advice.*;

/**
 * 切面
 *
 * @author warren
 */
public interface Aspect {

    /**
     * 源bean
     */
    BeforeAdvice getBeforeAdvice();

    /**
     * 前置通知
     */
    AroundAdvice getAroundAdvice();

    /**
     * 后置返回通知
     */
    AfterAdvice getAfterAdvice();

    /**
     * 后置返回通知
     */
    AfterReturningAdvice getAfterReturningAdvice();

    /**
     * 后置异常通知
     */
    ThrowsAdvice getAfterThrowingAdvice();

    /**
     * 切点
     */
    Pointcut getPointcut();

    static Aspect createAspect() {
        return new DefaultAnnotationAspect();
    }


    void setAspectBean(Object aspectBean);

    void setBeforeAdvice(BeforeAdvice beforeAdvice);

    void setAroundAdvice(AroundAdvice aroundAdvice);

    void setAfterAdvice(AfterAdvice afterAdvice);

    void setAfterReturningAdvice(AfterReturningAdvice afterReturningAdvice);

    void setAfterThrowingAdvice(ThrowsAdvice afterThrowingAdvice);

    void setPointcut(Pointcut pointcut);


    void initAspect(Class<?> clz,String name);


    void copy(ProxyConfig config);

}

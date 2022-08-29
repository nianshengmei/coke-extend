package org.needcoke.aop.proxy;

import org.aopalliance.aop.Advice;

/**
 * 切面
 *
 * @author warren
 */
public interface Aspect {

    /**
     * 源bean
     */
    Advice getBeforeAdvice();

    /**
     * 前置通知
     */
    Advice getAroundAdvice();

    /**
     * 后置返回通知
     */
    Advice getAfterAdvice();

    /**
     * 后置返回通知
     */
    Advice getAfterReturningAdvice();

    /**
     * 后置异常通知
     */
    Advice getAfterThrowingAdvice();

    /**
     * 切点
     */
    Pointcut getPointcut();

    static Aspect createAspect() {
        return new DefaultAnnotationAspect();
    }


    void setAspectBean(Object aspectBean);

    void setBeforeAdvice(Advice beforeAdvice);

    void setAroundAdvice(Advice aroundAdvice);

    void setAfterAdvice(Advice afterAdvice);

    void setAfterReturningAdvice(Advice afterReturningAdvice);

    void setAfterThrowingAdvice(Advice afterThrowingAdvice);

    void setPointcut(Pointcut pointcut);


    void initAspect(Class<?> clz);


}

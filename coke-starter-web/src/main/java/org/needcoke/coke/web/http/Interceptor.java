package org.needcoke.coke.web.http;

/**
 * 拦截器接口
 *
 * @author warren
 */
public interface Interceptor {

    /**
     * 前置拦截
     */
    boolean preHandle(CokeHttpContext context);

    /**
     * 后置拦截
     */
    void afterCompletion(CokeHttpContext context);


}
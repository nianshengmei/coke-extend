package org.needcoke.coke.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器接口
 *
 * @author warren
 */
public interface Interceptor {

    /**
     * 前置拦截
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * 后置拦截
     */
    void afterCompletion(HttpServletRequest request, HttpServletResponse response);


}
package org.needcoke.coke.web.http;

import pers.warren.ioc.core.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
public interface Handler {

    //执行方法
    void handle(HttpServletRequest request, HttpServletResponse response, ApplicationContext applicationContext) throws Throwable;
}

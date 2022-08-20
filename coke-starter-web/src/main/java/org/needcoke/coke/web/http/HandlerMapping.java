package org.needcoke.coke.web.http;

import pers.warren.ioc.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
public interface HandlerMapping {

    Handler getHandler(HttpServletRequest request, HttpServletResponse response);

    void mapping(HttpServletRequest request,HttpServletResponse response);

    HandlerAdapter getHandlerAdapter();
}

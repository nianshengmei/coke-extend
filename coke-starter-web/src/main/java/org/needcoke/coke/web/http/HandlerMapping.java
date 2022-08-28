package org.needcoke.coke.web.http;

import org.needcoke.coke.core.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
public interface HandlerMapping extends Order {

    Handler getHandler(HttpServletRequest request, HttpServletResponse response);

    boolean mapping(HttpServletRequest request,HttpServletResponse response);

    HandlerAdapter getHandlerAdapter();
}

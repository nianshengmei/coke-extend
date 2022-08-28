package org.needcoke.coke.web.http;

import org.needcoke.coke.core.Order;
import pers.warren.ioc.core.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 * @date 2022/4/2
 */
public abstract class AbstractHandler implements Handler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,ApplicationContext applicationContext) throws Throwable{
        handle(new HttpContext(request,response),applicationContext);
    }

    public abstract void handle(HttpContext ctx, ApplicationContext applicationContext) throws Throwable;
}

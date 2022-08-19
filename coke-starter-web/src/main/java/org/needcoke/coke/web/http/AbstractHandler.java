package org.needcoke.coke.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 * @date 2022/4/2
 */
public abstract class AbstractHandler implements Handler{


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        handle(new HttpContext(request,response));
    }

    public abstract void handle(HttpContext ctx);
}

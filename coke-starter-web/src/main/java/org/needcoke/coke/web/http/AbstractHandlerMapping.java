package org.needcoke.coke.web.http;

import org.needcoke.coke.core.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 * @date 2022/4/2
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {

    @Override
    public boolean mapping(HttpServletRequest request, HttpServletResponse response) {
        Handler handler = getHandler(request, response);
        if(null != handler){
            getHandlerAdapter().handle(request,response,handler);
            return true;
        }
        return false;
    }

    protected String getRequestUri(HttpServletRequest request){
        return request.getMethod()+" "+request.getRequestURI();
    }

    @Override
    public int getOrder() {
        return 5;
    }
}

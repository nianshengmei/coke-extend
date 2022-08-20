package org.needcoke.coke.web.http;

import pers.warren.ioc.annotation.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
@Component
public class PathVariableHandlerMapping extends AbstractHandlerMapping{

    @Override
    public Handler getHandler(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public HandlerAdapter getHandlerAdapter() {
        return null;
    }
}

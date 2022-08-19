package org.needcoke.coke.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 * @date 2022/4/2
 */
public abstract class AbstractHandlerMapping implements HandlerMapping{

    @Override
    public void mapping(HttpServletRequest request, HttpServletResponse response) {

    }
}

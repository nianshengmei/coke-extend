package org.needcoke.coke.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 * @date 2022/4/2
 */
public class RequestMappingHandlerMapping extends AbstractHandlerMapping{
    @Override
    public Handler getHandler(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

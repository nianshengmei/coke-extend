package org.needcoke.coke.web.http;

import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.core.WebApplicationContext;
import pers.warren.ioc.annotation.Autowired;
import pers.warren.ioc.annotation.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
@Component
@Slf4j
public class RequestMappingHandlerMapping extends AbstractHandlerMapping{
    @Resource
    private HandlerAdapter handlerAdapter;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Override
    public Handler getHandler(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = getRequestUri(request);
        return webApplicationContext.getHandler(requestUri);
    }

    @Override
    public HandlerAdapter getHandlerAdapter() {
        return handlerAdapter;
    }
}

package org.needcoke.coke.web.http;

import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.core.WebApplicationContext;
import pers.warren.ioc.annotation.Autowired;
import pers.warren.ioc.annotation.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author warren
 */
@Component
@Slf4j
public class PathVariableHandlerMapping extends AbstractHandlerMapping{
    @Resource
    private HandlerAdapter handlerAdapter;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Override
    public Handler getHandler(HttpServletRequest request, HttpServletResponse response) {
        List<PathVariableRequestMappingHandler> handlerList = webApplicationContext.getPathVariableRequestMappingHandlerList();
        for (PathVariableRequestMappingHandler handler : handlerList) {
            String[] httpRequestPathArr = request.getRequestURI().split("/");
            if(!handler.getHttpType().name().equals(request.getMethod())){
                continue;
            }
            if(handler.match(httpRequestPathArr)){
                return handler;
            }
        }

        return null;
    }

    @Override
    public HandlerAdapter getHandlerAdapter() {
        return handlerAdapter;
    }
}

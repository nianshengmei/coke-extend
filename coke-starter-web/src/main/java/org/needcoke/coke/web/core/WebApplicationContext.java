package org.needcoke.coke.web.core;

import org.needcoke.coke.web.http.Handler;
import org.needcoke.coke.web.http.PathVariableRequestMappingHandler;
import pers.warren.ioc.core.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author warren
 */
public class WebApplicationContext extends ApplicationContext {

    private final Map<String, Handler> requestMappingHandlerMap = new HashMap<>();

    private final List<PathVariableRequestMappingHandler> pathVariableRequestMappingHandlerList = new ArrayList<>();

    public void putHandler(String name ,Handler handler){
        if(handler instanceof PathVariableRequestMappingHandler){
            pathVariableRequestMappingHandlerList.add((PathVariableRequestMappingHandler) handler);
            return;
        }
        requestMappingHandlerMap.put(name,handler);
    }

    public Handler getHandler(String name){
        return requestMappingHandlerMap.get(name);
    }


    public List<PathVariableRequestMappingHandler> getPathVariableRequestMappingHandlerList(){
        return pathVariableRequestMappingHandlerList;
    }
}

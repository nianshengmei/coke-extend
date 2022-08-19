package org.needcoke.coke.web.core;

import org.needcoke.coke.web.http.Handler;
import pers.warren.ioc.core.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author warren
 */
public class WebApplicationContext extends ApplicationContext {

    private final Map<String, Handler> requestMappingHandlerMap = new HashMap<>();

    public void putHandler(String name ,Handler handler){
        requestMappingHandlerMap.put(name,handler);
    }

    public Handler getHandler(String name){
        return requestMappingHandlerMap.get(name);
    }

}

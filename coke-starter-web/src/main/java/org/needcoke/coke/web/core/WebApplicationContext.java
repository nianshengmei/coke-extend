package org.needcoke.coke.web.core;

import org.needcoke.coke.web.http.HttpType;
import pers.warren.ioc.core.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author warren
 */
public class WebApplicationContext extends ApplicationContext {


    private final Map<String, WebFunction> httpFunctionMap = new HashMap<>();

    //  GET user/get/{id}  ->id
    private final Map<String, String> pathVariableFunctionMap = new HashMap<>();

    public void addWebFunction(HttpType httpType, String requestUri, WebFunction webFunction) {
        httpFunctionMap.put(httpType.name() + " " + requestUri, webFunction);
    }

    public WebFunction getWebFunction(HttpType httpType, String requestUri) {
        return httpFunctionMap.get(httpType.name() + " " + requestUri);
    }

    public WebFunction getWebFunction(String fullUri) {
        return httpFunctionMap.get(fullUri);
    }

    public void addWebFunction(HttpType httpType, String requestUri, String invokeBeanName, Method invokeMethod) {
        String scanUri = httpType.name() + " " + requestUri;
        httpFunctionMap.put(scanUri,
                new WebFunction()
                        .setHttpType(httpType)
                        .setInvokeBeanName(invokeBeanName)
                        .setInvokeMethod(invokeMethod)
        );
        if (scanUri.contains("{") && scanUri.contains("}")) {

        }
    }
}

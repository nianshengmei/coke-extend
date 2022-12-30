package org.needcoke.coke.web.client;

import org.needcoke.coke.web.util.MethodUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpClientCacheMgmt {

    private final Map<String,HttpClientCache> cacheMap = new HashMap<>();

    private static final HttpClientCacheMgmt instance = new HttpClientCacheMgmt();

    public static HttpClientCacheMgmt getInstance(){
        return instance;
    }

    public void addCache(Class<?> clientInterface, Method method,HttpClientCache clientCache){
        String key = clientInterface.getTypeName()+"-"+MethodUtil.getOnlyName(method);
        cacheMap.put(key,clientCache);
    }

    public HttpClientCache get(Class<?> clientInterface, Method method){
        String key = clientInterface.getTypeName()+"-"+MethodUtil.getOnlyName(method);
        return cacheMap.get(key);
    }
}

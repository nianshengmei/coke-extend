package org.needcoke.coke.web.interceptor;

import java.util.*;

public enum InterceptorCacheMgmt {

    instance;

    private final Map<String, List<String>> cacheMap = new HashMap<>();

    public void addCache(String pattern,String interceptorName){
        if(!cacheMap.containsKey(pattern)){
            cacheMap.put(pattern,new ArrayList<>());
        }
        cacheMap.get(pattern).add(interceptorName);
    }

    public List<String> get(String pattern){
        return cacheMap.get(pattern);
    }

    public Set<String> keySet(){
        return cacheMap.keySet();
    }
}

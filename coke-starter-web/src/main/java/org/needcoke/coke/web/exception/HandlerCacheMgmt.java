package org.needcoke.coke.web.exception;

import java.util.HashMap;
import java.util.Map;

public enum HandlerCacheMgmt {
        instance
    ;

    private final Map<String,HandlerCache> cacheMap = new HashMap<>();

    public void addCache(HandlerCache cache){
        cacheMap.put(cache.getExceptionType().getTypeName(),cache);
    }

    public HandlerCache get(Class<? extends Throwable> throwableClz){
        return cacheMap.get(throwableClz.getTypeName());
    }

    public boolean contains(Class<? extends Throwable> throwableClz){
        return cacheMap.containsKey(throwableClz.getTypeName());
    }
}

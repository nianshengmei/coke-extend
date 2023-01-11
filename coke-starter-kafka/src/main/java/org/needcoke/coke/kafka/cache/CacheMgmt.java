package org.needcoke.coke.kafka.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CacheMgmt {

    instance;

    private final Map<String, EventCache> cacheMap = new ConcurrentHashMap<>();

    public void put(EventCache cache) {
        cacheMap.put(cache.getTopic(), cache);
    }

    public EventCache get(String topic) {
        return cacheMap.get(topic);
    }

    public boolean contains(String topic) {
        return cacheMap.containsKey(topic);
    }

}

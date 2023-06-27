package org.needcoke.data.core;


/**
 * 缓存的类型
 *
 * @author warren
 * @since 1.0.3
 */
public enum CacheType {

    LOCAL("local"),

    REDIS("redis");

    private String type;

    CacheType(String type) {
        this.type = type;
    }
}

package org.needcoke.coke.web.constant;

/**
 * @author warren
 * @date 2022/4/2
 */
public interface ThreadPoolDefaultValue {

    /**
     * 默认的核心线程池大小
     */
    int CORE_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程大小
     */
    int MAX_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 保活时间
     */
    long KEEP_ALIVE_TIME = 200;

}

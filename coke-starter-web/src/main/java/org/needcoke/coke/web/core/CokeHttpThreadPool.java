package org.needcoke.coke.web.core;

/**
 * coke-web核心线程池
 *
 * @author warren
 */

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.constant.ThreadPoolDefaultValue;
import pers.warren.ioc.annotation.Autowired;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.annotation.Value;

import java.util.concurrent.*;

@Component
@Slf4j
public class CokeHttpThreadPool {

    public CokeHttpThreadPool() {
        log.info("web thread pool start ok ！core pool size = {},max pool size = {}", iocCoreThreadPoolSize, iocMaximumPoolSize);
    }

    @Autowired
    private static CokeHttpThreadPool threadPool;

    public static CokeHttpThreadPool getCoreThreadPool() {
        return CokeHttpThreadPool.threadPool;
    }

    /**
     * 自定义线程名称,方便的出错的时候溯源
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("test-pool-%d").build();

    @Value("coke.web.pool.coreSize")
    private int iocCoreThreadPoolSize = ThreadPoolDefaultValue.CORE_SIZE;

    @Value("coke.web.pool.maxSize")
    private int iocMaximumPoolSize = ThreadPoolDefaultValue.MAX_SIZE;

    @Value("coke.web.pool.keepAliveTime")
    private long iocKeepAliveTime = ThreadPoolDefaultValue.KEEP_ALIVE_TIME;


    /**
     * corePoolSize    线程池核心池的大小
     * maximumPoolSize 线程池中允许的最大线程数量
     * keepAliveTime   当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
     * unit            keepAliveTime 的时间单位
     * workQueue       用来储存等待执行任务的队列
     * threadFactory   创建线程的工厂类
     * handler         拒绝策略类,当线程池数量达到上线并且workQueue队列长度达到上限时就需要对到来的任务做拒绝处理
     */
    private ExecutorService service = new ThreadPoolExecutor(
            iocCoreThreadPoolSize,
            iocMaximumPoolSize,
            iocKeepAliveTime,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    public ExecutorService getExecutorService() {
        return service;
    }

    /**
     * 使用线程池创建线程并异步执行任务
     *
     * @param r 任务
     */
    public void newTask(Runnable r) {
        service.execute(r);
    }

    @Override
    public String toString() {
        return "ThreadPool{iocCoreThreadPoolSize = " + iocCoreThreadPoolSize + " , iocMaximumPoolSize = " + iocMaximumPoolSize + "" +
                " , iocKeepAliveTime = " + iocKeepAliveTime + " }";
    }

}
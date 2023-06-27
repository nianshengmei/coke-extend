package org.needcoke.data.config;

import lombok.Data;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;
import java.util.List;

/**
 *
 */
@Data
@Configuration
public class RedisConfig {

    public static final String REDIS_MODE_SINGLE = "single";       //单点模式

    public static final String REDIS_MODE_CLUSTER = "cluster";     //集群模式

    public static final String REDIS_MODE_SENTINEL = "sentinel";   //哨兵模式

    public static final String REDIS_MODE_MASTER_SLAVE = "master_slave"; //主从模式

    public static final String REDIS_MODE_REPLICATION = "replicated"; //云托管模式

    /**
     * 配置的Redis的模式
     *
     * @since 1.0.3
     */

    @Value("coke.data.redis.mode")
    private String mode;

    /**
     * 节点地址
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.nodeAddresses")
    private List<String> nodeAddresses;

    /**
     * 集群扫描间隔时间
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.scanInterval:1000")
    private Long scanInterval;

    /**
     * 传输模式
     * <p>NIO , EPOLL, KQUEUE</p>
     *
     * @since 1.0.3
     */

    @Value("coke.data.redis.transport-mode")
    private String transportMode;

    /**
     * 监控锁的看门狗超时时间单位为毫秒。该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况。
     * 如果该看门口未使用lockWatchdogTimeout去重新调整一个分布式锁的lockWatchdogTimeout超时，那么这个锁将变为失效状态。
     * 这个参数可以用来避免由Redisson客户端节点宕机或其他原因造成死锁的情况
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.lock-watchdog-timeout:30000")
    private Long lockWatchdogTimeout;

    /**
     * 保持订阅顺序
     *
     * <p>通过该参数来修改是否按订阅发布消息的接收顺序出来消息，如果选否将对消息实行并行处理，该参数只适用于订阅发布消息的情况。</p>
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.keep-pub-sub-order:true")
    private Boolean keepPubSubOrder;

    /**
     * 用来指定高性能引擎的行为。由于该变量值的选用与使用场景息息相关（NORMAL除外）我们建议对每个参数值都进行尝试。
     * <p>
     * 该参数仅限于Redisson PRO版本。
     * <p>
     * 可选模式：
     * HIGHER_THROUGHPUT - 将高性能引擎切换到 高通量 模式。
     * LOWER_LATENCY_AUTO - 将高性能引擎切换到 低延时 模式并自动探测最佳设定。
     * LOWER_LATENCY_MODE_1 - 将高性能引擎切换到 低延时 模式并调整到预设模式1。
     * LOWER_LATENCY_MODE_2 - 将高性能引擎切换到 低延时 模式并调整到预设模式2。
     * NORMAL - 将高性能引擎切换到 普通 模式
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.performance-mode:HIGHER_THROUGHPUT")
    private String performanceMode;

    /**
     * 将高性能引擎切换到 高通量 模式
     */
    public static final String HIGHER_THROUGHPUT = "HIGHER_THROUGHPUT";

    /**
     * LOWER_LATENCY_AUTO 将高性能引擎切换到 低延时 模式并自动探测最佳设定
     */
    public static final String LOWER_LATENCY_AUTO = "LOWER_LATENCY_AUTO";

    /**
     * 分片数量
     *
     * <p>用于指定数据分片过程中的分片数量。支持数据分片/框架结构有：集（Set）、映射（Map）、BitSet、Bloom filter, Spring Cache和Hibernate Cache等.</p>
     */
    @Value("coke.data.redis.slots:231")
    private Integer slots;

    /**
     * 在从服务节点里读取的数据说明已经至少有两个节点保存了该数据，确保了数据的高可用性
     *
     * <p>设置读取操作选择节点的模式。 可用值为： SLAVE - 只在从服务节点里读取。 MASTER - 只在主服务节点里读取。 MASTER_SLAVE - 在主从服务节点里都可以读取</p>
     */
    @Value("coke.data.redis.read-mode:SLAVE")
    private String readMode;

    /**
     * 只在从服务节点里读取
     */
    public static final String READ_MODE_SLAVE = "SLAVE";

    /**
     * 只在主服务节点里读取
     */
    public static final String READ_MODE_MASTER = "MASTER";

    /**
     * 在主从服务节点里都可以读取
     */
    public static final String READ_MODE_MASTER_SLAVE = "MASTER_SLAVE";

    /**
     * 订阅操作的负载均衡模式
     *
     * <p>默认值：SLAVE（只在从服务节点里订阅）</p>
     *
     * <p>设置订阅操作选择节点的模式。 可用值为： SLAVE - 只在从服务节点里订阅。 MASTER - 只在主服务节点里订阅。</p>
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.subscription.mode:SLAVE")
    private String subscriptionMode;

    /**
     * 负载均衡算法类的选择
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.load-balance:org.redisson.connection.balancer.RoundRobinLoadBalancer")
    private String loadBalancer;

    /**
     * 轮询调度算法
     */
    public static final String LB_ROUND_ROBIN_LOAD_BALANCER = "org.redisson.connection.balancer.RoundRobinLoadBalancer";

    /**
     * 随机调度算法
     */
    public static final String LB_RANDOM_LOAD_BALANCER = "org.redisson.connection.balancer.RandomLoadBalancer";

    /**
     * 权重轮询调度算法
     */
    public static final String LB_WEIGHTED_ROUND_ROBIN_BALANCER = "org.redisson.connection.balancer.WeightedRoundRobinBalancer";

    /**
     * 从节点发布和订阅连接的最小空闲连接数
     *
     * @since 1.0.3
     */
    private Integer subscriptionConnectionMinimumIdleSize;

    private Integer subscriptionConnectionPoolSize;

    private Integer slaveConnectionMinimumIdleSize;

    private Integer slaveConnectionPoolSize;

    private Integer masterConnectionMinimumIdleSize;

    private Integer masterConnectionPoolSize;

    private Long idleConnectionTimeout;

    private Long connectTimeout;

    private Long timeout;

    private Integer retryAttempts;

    private Long retryInterval;

    private String password;

    private Integer subscriptionsPerConnection;

    private String clientName;

    private Boolean sslEnableEndpointIdentification;

    private String sslProvider;




}

package org.needcoke.data.config;

import lombok.Data;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;

import java.util.List;

/**
 * 关于redisson的一些配置信息
 *
 * @author warren
 * @since 1.0.3
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
     * <p>默认值：org.redisson.connection.balancer.RoundRobinLoadBalancer</p>
     *
     * <p>在使用多个Elasticache Redis服务节点的环境里，可以选用以下几种负载均衡方式选择一个节点： </p>
     * <p>org.redisson.connection.balancer.WeightedRoundRobinBalancer - 权重轮询调度算法 </p>
     * <p>org.redisson.connection.balancer.RoundRobinLoadBalancer - 轮询调度算法 </p>
     * <p>org.redisson.connection.balancer.RandomLoadBalancer - 随机调度算法</p>
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
     * 默认值：5000
     * <p>
     * 用来指定检查节点DNS变化的时间间隔。使用的时候应该确保JVM里的DNS数据的缓存时间保持在足够低的范围才有意义。用-1来禁用该功能。
     */
    @Value("coke.data.redis.dns-monitor-interval:5000")
    private Long dnsMonitoringInterval;

    /**
     * 从节点发布和订阅连接的最小空闲连接数
     * <p>默认值：1
     * <p>
     * 多从节点的环境里，每个 从服务节点里用于发布和订阅连接的最小保持连接数（长连接）。Redisson内部经常通过发布和订阅来实现许多功能。长期保持一定数量的发布订阅连接是必须的。</p>
     *
     * @since 1.0.3
     */
    @Value("coke.data.redis.subscription-connection-minimum-idle-size:1")
    private Integer subscriptionConnectionMinimumIdleSize;

    /**
     * 从节点发布和订阅连接池大小
     * 默认值：50
     * <p>
     * 多从节点的环境里，每个 从服务节点里用于发布和订阅连接的连接池最大容量。连接池的连接数量自动弹性伸缩。
     */
    @Value("coke.data.redis.subscription-connection-pool-size:50")
    private Integer subscriptionConnectionPoolSize;

    /**
     * 从节点最小空闲连接数
     * <p>默认值：32</p>
     * <p>多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）的最小保持连接数（长连接）。长期保持一定数量的连接有利于提高瞬时读取反映速度</p>
     */
    @Value("coke.data.redis.slave-connection-minimum-idle-size:32")
    private Integer slaveConnectionMinimumIdleSize;

    /**
     * 从节点连接池大小
     * <p>默认值：64</p>
     * <p>多从节点的环境里，每个 从服务节点里用于普通操作（非 发布和订阅）连接的连接池最大容量。连接池的连接数量自动弹性伸缩。</p>
     */
    @Value("coke.data.redis.slave-connection-pool-size:64")
    private Integer slaveConnectionPoolSize;

    /**
     * 主节点最小空闲连接数
     * <p>
     * 默认值：32
     * <p>
     * 多从节点的环境里，每个 主节点的最小保持连接数（长连接）。长期保持一定数量的连接有利于提高瞬时写入反应速度。
     */
    @Value("coke.data.redis.master-connection-minimum-idle-size:32")
    private Integer masterConnectionMinimumIdleSize;

    /**
     * 主节点连接池大小
     * 默认值：64
     * <p>
     * 主节点的连接池最大容量。连接池的连接数量自动弹性伸缩。
     */
    @Value("coke.data.redis.master-connection-pool-size:64")
    private Integer masterConnectionPoolSize;

    /**
     * 连接空闲超时，单位：毫秒
     */
    @Value("coke.data.redis.idle-connection-timeout:10000")
    private Long idleConnectionTimeout;

    /**
     * 连接超时，单位：毫秒
     */
    @Value("coke.data.redis.connect-timeout:10000")
    private Long connectTimeout;

    /**
     * 命令等待超时，单位：毫秒
     */
    @Value("coke.data.redis.timeout:3000")
    private Long timeout;

    /**
     * 命令失败重试次数
     *
     * <p>默认值：3</p>
     * <p>如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。</p>
     */
    @Value("coke.data.redis.retry-attempts:3")
    private Integer retryAttempts;

    /**
     * 命令重试发送时间间隔，单位：毫秒
     * <p>
     * 默认值：1500
     * <p>
     * 在某个节点执行相同或不同命令时，连续 失败 failedAttempts（执行失败最大次数） 时，该节点将被从可用节点列表里清除，直到 reconnectionTimeout（重新连接时间间隔） 超时以后再次尝试。
     */
    @Value("coke.data.redis.retry-interval:1500")
    private Long retryInterval;

    /**
     * 默认值：null
     * <p>
     * 用于节点身份验证的密码。
     */
    @Value("coke.data.redis.password")
    private String password;

    /**
     * 默认值：5
     * <p>
     * 每个连接的最大订阅数量。
     */
    @Value("coke.data.redis.subscriptions-per-connection:5")
    private Integer subscriptionsPerConnection;

    /**
     * 默认值：null
     * <p>
     * 在Redis节点里显示的客户端名称。
     */
    @Value("coke.data.redis.client-name:coke-client")
    private String clientName;

    /**
     * 默认值：true
     * <p>
     * 开启SSL终端识别能力。
     */
    @Value("coke.data.redis.ssl-enable-endpoint-identification:true")
    private Boolean sslEnableEndpointIdentification;

    /**
     * 默认值：JDK
     * <p>
     * 确定采用哪种方式（JDK或OPENSSL）来实现SSL连接。
     */
    @Value("coke.data.redis.ssl-provider:JDK")
    private String sslProvider;

    public static final String SSL_PROVIDER_JDK = "JDK";

    public static final String SSL_PROVIDER_OPENSSL = "OPENSSL";

    /**
     * 指定SSL信任证书库的路径。
     */
    @Value("coke.data.redis.ssl-truststore")
    private String sslTruststore;

    /**
     * SSL信任证书库密码
     */
    @Value("coke.data.redis.ssl-truststore-password")
    private String sslTruststorePassword;

    /**
     * SSL钥匙库路径
     */
    @Value("coke.data.redis.ssl-keystore")
    private String sslKeystore;

    /**
     * SSL钥匙库密码
     */
    @Value("coke.data.redis.ssl-keystore-password")
    private String sslKeystorePassword;

    /**
     * 最小空闲连接数，单节点模式
     * <p>
     * 默认值：32
     * <p>
     * 最小保持连接数（长连接）。长期保持一定数量的连接有利于提高瞬时写入反应速度。
     */
    @Value("coke.data.redis.connection-minimum-idle-size:32")
    private Integer connectionMinimumIdleSize;

    /**
     * 默认值：64
     * <p>
     * 连接池大小
     * <p>
     * 单节点模式
     */
    @Value("coke.data.redis.connection-pool-size:64")
    private Integer connectionPoolSize;
}

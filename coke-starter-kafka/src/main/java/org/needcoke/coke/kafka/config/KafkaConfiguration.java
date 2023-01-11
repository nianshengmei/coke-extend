package org.needcoke.coke.kafka.config;

import cn.hutool.core.util.StrUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.needcoke.coke.kafka.DefaultKafkaConsumer;
import org.needcoke.coke.kafka.DefaultKafkaProducer;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;
import pers.warren.ioc.condition.ConditionalOnBean;
import pers.warren.ioc.condition.ConditionalOnProperties;
import pers.warren.ioc.condition.ConditionalProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Bean
    @ConditionalOnProperties(@ConditionalProperty("coke.kafka.broker-list"))
    public DefaultKafkaProducer kafkaProducer(Properties kafkaProducerProperties) {
        return new DefaultKafkaProducer(kafkaProducerProperties);
    }

    @Bean
    @ConditionalOnProperties(@ConditionalProperty("coke.kafka.broker-list"))
    public DefaultKafkaConsumer kafkaConsumer(Properties kafkaConsumerProperties) {
        return new DefaultKafkaConsumer(kafkaConsumerProperties);
    }

    @Bean
    @ConditionalOnProperties(@ConditionalProperty("coke.kafka.broker-list"))
    public Map<String, KafkaConsumer<String,String>> kafkaConsumerMap(){
        return new HashMap<>();
    }

    @Bean
    @ConditionalOnBean(DefaultKafkaProducer.class)
    public Properties kafkaProducerProperties() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                valueSerializer);
        props.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.ACKS_CONFIG, acks);

        //拦截器
        if (StrUtil.isNotEmpty(interceptorClasses)) {
            String[] split = interceptorClasses.split(",");
            props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, split);
        }
        //分区管理
        if (StrUtil.isNotEmpty(partitionClasses)) {
            String[] split = partitionClasses.split(",");
            props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, split);
        }
        return props;
    }


    @Value("coke.kafka.broker-list")
    private String brokerList;

    @Value("coke.kafka.producer.key-serializer:org.apache.kafka.common.serialization.StringSerializer")
    private String keySerializer;

    @Value("coke.kafka.producer.value-serializer:org.apache.kafka.common.serialization.StringSerializer")
    private String valueSerializer;

    /**
     * 客户端编号
     */
    @Value("coke.kafka.client-id")
    private String clientId;

    @Value("coke.kafka.producer.interceptor-classes")            //拦截器
    private String interceptorClasses;

    /**
     * ack=0， 生产者在成功写入消息之前不会等待任何来自服务器的相应。如果出现问题生产者是感知 不到的，消息就丢失了。不过因为生产者不需要等待服务器响应，所以它可以以网络能够支持的最 大速度发送消息，从而达到很高的吞吐量。
     * <p>
     * ack=1，默认值为1，只要集群的首领节点收到消息，生产这就会收到一个来自服务器的成功响 应。如果消息无法达到首领节点（比如首领节点崩溃，新的首领还没有被选举出来），生产者会收 到一个错误响应，为了避免数据丢失，生产者会重发消息。但是，这样还有可能会导致数据丢失， 如果收到写成功通知，此时首领节点还没来的及同步数据到follower节点，首领节点崩溃，就会导 致数据丢失。
     * <p>
     * ack=-1， 只有当所有参与复制的节点都收到消息时，生产这会收到一个来自服务器的成功响应， 这种模式是最安全的，它可以保证不止一个服务器收到消息
     */
    @Value("coke.kafka.producer.acks:1")
    private int acks;

    @Value("coke.kafka.producer.partition-classes")    //分区器
    private String partitionClasses;

    @Value("coke.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.StringDeserializer")
    private String keyDeserializer;

    @Value("coke.kafka.consumer.value-deserializer:org.apache.kafka.common.serialization.StringDeserializer")
    private String valueDeserializer;

    @Value("coke.kafka.consumer.enable-auto-commit:true")
    private Boolean autoCommit;

    @Value("coke.kafka.consumer.auto-commit-interval-ms:1000")
    private Long autoCommitIntervalMs;

    @Value("coke.kafka.consumer.session-time-ms:30000")
    private String sessionTimeoutMs;

    @Bean
    @ConditionalOnBean(DefaultKafkaProducer.class)
    public Properties kafkaConsumerProperties() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                keyDeserializer);
        props.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        return props;
    }


}

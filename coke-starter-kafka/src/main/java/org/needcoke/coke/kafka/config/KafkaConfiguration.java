package org.needcoke.coke.kafka.config;

import cn.hutool.core.util.StrUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.needcoke.coke.kafka.DefaultKafkaProducer;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;
import pers.warren.ioc.condition.ConditionalOnBean;
import pers.warren.ioc.condition.ConditionalOnProperties;
import pers.warren.ioc.condition.ConditionalProperty;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Bean
    @ConditionalOnProperties(@ConditionalProperty("coke.kafka.broker-list"))
    public DefaultKafkaProducer kafkaProducer(Properties kafkaProperties){
        return new DefaultKafkaProducer(kafkaProperties);
    }

    @Bean
    @ConditionalOnBean(DefaultKafkaProducer.class)
    public Properties kafkaProperties(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                valueSerializer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.ACKS_CONFIG,ack);

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

    @Value("coke.kafka.key-serializer:org.apache.kafka.common.serialization.StringSerializer")
    private String keySerializer;

    @Value("coke.kafka.value-serializer:org.apache.kafka.common.serialization.StringSerializer")
    private String valueSerializer;

    @Value("coke.kafka.client-id")
    private String clientId;

    @Value("coke.kafka.interceptor-classes")            //拦截器
    private String interceptorClasses;

    @Value("coke.kafka.acks:0")
    private int ack;

    @Value("coke.kafka.partition-classes")    //分区器
    private String partitionClasses;
}

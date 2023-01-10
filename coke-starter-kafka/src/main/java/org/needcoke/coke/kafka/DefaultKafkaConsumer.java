package org.needcoke.coke.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.needcoke.coke.kafka.config.KafkaConsumerRebalanceListener;
import pers.warren.ioc.annotation.Init;

import javax.annotation.Resource;
import java.util.*;

public class DefaultKafkaConsumer {

    @Resource
    private Properties kafkaConsumerProperties;

    @Resource
    private Map<String, KafkaConsumer<String,String>> kafkaConsumerMap;


    @Init
    void init() {
        kafkaConsumerMap.put("-1*#*#DefaultConsumer", defaultKafkaConsumer);
    }

    private final KafkaConsumer<String,String> defaultKafkaConsumer;

    public DefaultKafkaConsumer(Properties kafkaConsumerProperties) {
        this.kafkaConsumerProperties = kafkaConsumerProperties;
        defaultKafkaConsumer = new KafkaConsumer<>(kafkaConsumerProperties);
    }

    /**
     * 订阅 topic
     */
    public void subscribe(String topic) {
        defaultKafkaConsumer.subscribe(Collections.singletonList(topic), new KafkaConsumerRebalanceListener());
    }

    /**
     * 订阅 topic
     */
    public void subscribe(String topic,String group) {
        if(!kafkaConsumerMap.containsKey(group)) {
            Properties properties = new Properties();
            Set<Object> keySet = kafkaConsumerProperties.keySet();
            for (Object o : keySet) {
                properties.put(o, kafkaConsumerProperties.get(o));
            }
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
            kafkaConsumerMap.put(group, kafkaConsumer);
        }
        kafkaConsumerMap.get(group).subscribe(Collections.singletonList(topic), new KafkaConsumerRebalanceListener());
    }

}

package org.needcoke.coke.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.Properties;

/**
 * kafka生产者
 */
public class DefaultKafkaProducer {

    private final KafkaProducer<String,String> producer;

    public DefaultKafkaProducer(Properties properties) {
        this.producer = new KafkaProducer<>(properties);
    }



}

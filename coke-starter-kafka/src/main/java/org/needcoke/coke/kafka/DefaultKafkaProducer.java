package org.needcoke.coke.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * kafka生产者
 */
public class DefaultKafkaProducer {

    private final KafkaProducer<String, Object> producer;

    public DefaultKafkaProducer(Properties properties) {
        this.producer = new KafkaProducer<>(properties);
    }

    public Future<RecordMetadata> send(ProducerRecord<String, Object> record) {
        return producer.send(record);
    }

    public Future<RecordMetadata> send(ProducerRecord<String, Object> record, Callback callback) {
        return producer.send(record, callback);
    }

}

package org.needcoke.coke.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.util.SerializeUtil;

import javax.annotation.Resource;
import java.util.concurrent.Future;


/**
 * 事件总线
 * @author warren
 */
@Component
@Slf4j
public class EventBus {

    @Resource
    private DefaultKafkaProducer kafkaProducer;

    /**
     * 同步发送
     */
    public int sendSync(String topic, Object o) {
        Future<RecordMetadata> future = send(topic, o, null);
        try {
            RecordMetadata recordMetadata = future.get();
            log.info("event bus - send sync {}", recordMetadata);
            return recordMetadata.partition();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步发送
     */
    public int sendSync(String topic, Object o, String logId) {
        Future<RecordMetadata> future = send(topic, o, null);
        try {
            RecordMetadata recordMetadata = future.get();
            log.info("event bus - send logId = {} , sync {}", logId, recordMetadata);
            return recordMetadata.partition();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步发送
     *
     * @param partition 指定分区，或者null为不指定
     */
    public int sendSync(String topic, Object o, Integer partition) {
        Future<RecordMetadata> future = send(topic, o, partition);
        try {
            RecordMetadata recordMetadata = future.get();
            log.info("event bus - send sync {}", recordMetadata);
            return recordMetadata.partition();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步发送
     *
     * @param partition 指定分区，或者null为不指定
     */
    public int sendSync(String topic, Object o, String logId, Integer partition) {
        Future<RecordMetadata> future = send(topic, o, partition);
        try {
            RecordMetadata recordMetadata = future.get();
            log.info("event bus - send logId = {} , sync {}", logId, recordMetadata);
            return recordMetadata.partition();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步发送
     */
    public void sendAsync(String topic, Object o) {
        send(topic, o, null);
    }

    /**
     * 异步发送
     */
    public void sendAsync(String topic, Object o, Callback callback) {
        send(topic, o, callback, null);
    }

    /**
     * 异步发送
     *
     * @param partition 指定分区，或者null为不指定
     */
    public void sendAsync(String topic, Object o, Integer partition) {
        send(topic, o, partition);
    }

    /**
     * 异步发送
     *
     * @param partition 指定分区，或者null为不指定
     */
    public void sendAsync(String topic, Object o, Callback callback, Integer partition) {
        send(topic, o, callback, partition);
    }


    /**
     * 灵活发送API
     *
     * @param partition 指定分区，或者null为不指定
     */
    public Future<RecordMetadata> send(String topic, Object o, Integer partition) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, partition, null, null, o, null);
        return kafkaProducer.send(record);
    }

    /**
     * 灵活发送API
     *
     * @param partition 指定分区，或者null为不指定
     */
    public Future<RecordMetadata> send(String topic, Object o, Callback callback, Integer partition) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, partition, null, null, o, null);
        return kafkaProducer.send(record, callback);
    }
}

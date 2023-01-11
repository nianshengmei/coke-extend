package org.needcoke.coke.kafka.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.needcoke.coke.kafka.CokeEvent;
import org.needcoke.coke.kafka.cache.CacheMgmt;
import org.needcoke.coke.kafka.cache.EventCache;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.ApplicationContext;
import pers.warren.ioc.handler.CokePostHandler;
import pers.warren.ioc.pool.CokeThreadPool;
import pers.warren.ioc.util.SerializeUtil;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

@Component
public class KafkaPostHandler implements CokePostHandler {

    @Resource
    private CokeThreadPool cokeThreadPool;

    @Resource
    private Map<String, KafkaConsumer<String, String>> kafkaConsumerMap;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CokeThreadPool threadPool;

    @Override
    public void run() throws Throwable {

        final KafkaConsumer<String, String>[] consumerArray = new KafkaConsumer[kafkaConsumerMap.size()];
        Collection<KafkaConsumer<String, String>> values = kafkaConsumerMap.values();
        values.toArray(consumerArray);
        new Thread(() -> {
            while (true) {
                for (KafkaConsumer<String, String> kafkaConsumer : consumerArray) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
                    if (!records.isEmpty()) {
                        for (ConsumerRecord<String, String> record : records) {
                            String message = record.value();
                            String topic = record.topic();
                            long offset = record.offset();
                            long timestamp = record.timestamp();
                            int partition = record.partition();
                            EventCache cache = CacheMgmt.instance.get(topic);
                            if (null != cache) {
                                String beanName = cache.getBeanName();
                                Object bean = applicationContext.getBean(beanName);
                                CokeEvent event = SerializeUtil.fromJson(message, cache.getEvtType());
                                event.setOffset(offset).setPartition(partition).setTimestamp(timestamp).setTopic(topic);
                                threadPool.newTask(() -> {
                                    try {
                                        cache.getMethod().invoke(bean, event);
                                    } catch (Exception e) {
                                        throw new RuntimeException(String.format("事件处理异常 message %s , event info %s", message, event));
                                    }
                                });

                            }

                        }
                    }
                }
            }
        }).start();
    }
}

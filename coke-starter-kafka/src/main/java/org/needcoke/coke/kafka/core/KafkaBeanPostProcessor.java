package org.needcoke.coke.kafka.core;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.kafka.CokeEvent;
import org.needcoke.coke.kafka.DefaultKafkaConsumer;
import org.needcoke.coke.kafka.annotation.EventListener;
import org.needcoke.coke.kafka.cache.CacheMgmt;
import org.needcoke.coke.kafka.cache.CacheType;
import org.needcoke.coke.kafka.cache.EventCache;
import org.needcoke.coke.kafka.exc.TopicException;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.util.ReflectUtil;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class KafkaBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private DefaultKafkaConsumer kafkaConsumer;
    @Override
    public void postProcessAfterBeforeProcessor(BeanDefinition beanDefinition, BeanRegister register) {
        Method[] declaredMethods = beanDefinition.getClz().getDeclaredMethods();
        List<Method> listenerMethods = Arrays.stream(declaredMethods).filter(m -> ReflectUtil.containsAnnotation(m, EventListener.class))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(listenerMethods)) {
            return;
        }
        for (Method listenerMethod : listenerMethods) {
            EventListener listener = listenerMethod.getAnnotation(EventListener.class);
            if(listenerMethod.getParameterTypes().length == 1 && CokeEvent.class.isAssignableFrom(listenerMethod.getParameterTypes()[0])){
                EventCache cache = new EventCache(beanDefinition.getName(), listener.topic(), listener.group(), listener.partition(), listenerMethod,(Class<? extends CokeEvent>) listenerMethod.getParameterTypes()[0], CacheType.CONSUMER);
                if (CacheMgmt.instance.contains(listener.topic())) {
                    String message = String.format("同一个topic {%s}不能在一个application重复被消费", listener.topic());
                    throw new TopicException(message);
                }
                CacheMgmt.instance.put(cache);
                kafkaConsumer.subscribe(listener.topic());
                log.info("订阅 kafka topic {} , group {} , partition {} 。", cache.getTopic(), cache.getGroup(), cache.getPartition());
            } else {
                throw new TopicException("@EventListener method must have only one parameter type instance of org.needcoke.coke.kafka。CokeEvent");
            }

        }
    }


}

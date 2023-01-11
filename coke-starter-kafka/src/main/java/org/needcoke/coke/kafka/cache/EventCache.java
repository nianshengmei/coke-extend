package org.needcoke.coke.kafka.cache;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.needcoke.coke.kafka.CokeEvent;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class EventCache {

    private String beanName;

    private String topic;

    private String group;   //消费组

    private int partition;  //分区

    private Method method;

    private Class<? extends CokeEvent> evtType;

    private CacheType type;
}

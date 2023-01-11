package org.needcoke.coke.data.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.handler.CokePostHandler;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class RedisPostHandler implements CokePostHandler {
    @Override
    public void run() throws Throwable {
        Container container = Container.getContainer();
        Field componentMapField = Container.class.getDeclaredField("componentMap");
        componentMapField.setAccessible(true);
        Map<String, Object> componentMap = (Map<String, Object>)componentMapField.get(container);
        Config config = new Config();

        //集群模式


        //单例模式


        //哨兵模式


        //主从模式

        //RedissonClient redissonClient = Redisson.create(config);
        //componentMap.put("coreRedisClient",redissonClient);

    }
}

package org.needcoke.coke.data.redis;

import org.redisson.client.RedisClient;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;

@Configuration
public class RedisConfig {




    @Bean(name = "coreRedisClient")
    public RedisClient redisClient(){
        return null;
    }
}

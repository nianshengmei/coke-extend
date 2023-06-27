package org.needcoke.coke.data.redis;

import org.needcoke.data.config.RedisConfig;
import org.needcoke.data.core.RedisTemplate;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.condition.ConditionalOnBean;

import java.util.Optional;

@Configuration
public class RedisConfiguration {

    @Bean(name = "redissonConfig")
    public Config config(RedisConfig redisConfig){
        Config config = new Config();
        String transportMode = redisConfig.getTransportMode();
        switch (transportMode) {
            case RedisConfig.REDIS_MODE_SINGLE:
                config.useSingleServer().setAddress(Optional.ofNullable(redisConfig.getNodeAddresses()).orElseThrow(RuntimeException::new).get(0));

        }
        return config;
    }


    @Bean(name = "coreRedisClient")
    @ConditionalOnBean(Config.class)
    public RedissonClient redisClient(Config redissonConfig){
        return Redisson.create(redissonConfig);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedisTemplate defaultRedisTemplate(RedisClient redisClient){
        return null;
    }
}

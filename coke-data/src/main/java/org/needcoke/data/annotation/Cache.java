package org.needcoke.data.annotation;

import org.needcoke.data.core.CacheType;

/**
 * Coke cache缓存注解
 *
 * <p>使用方法：该注解用于方法上,检索本地缓存中是否存在符合expression生成的key的缓存，
 * 有的话，就会直接从缓存中取，没有的话会执行方法，并将结果存入缓存中</p>
 *
 * @author warren
 * @since 1.0.3
 */
public @interface Cache {

    /**
     * 缓存表达式
     *
     * <p>表达式的格式为：</p>
     *
     * <p>以 User getUser(User user);   传参 {"id":"S001","name":"tom","brand":"1001"}为例</p>
     *
     * <p>表达式为：#{method.name}:#user.id:#user.brand</p>
     * <p>最终生成的key为  'getUser:S001:1001'</p>
     *
     * @since 1.0.3
     */

    String expression() default "";


    /**
     * 过期时间
     *
     * <p>默认值 1分钟</p>
     *
     * @since 1.0.3
     */
    long expireTime() default 60 * 1000L;

    /**
     * 缓存类型
     *
     * <p>默认本地缓存</p>
     *
     * @since 1.0.3
     */
    CacheType cacheType() default CacheType.LOCAL;
}

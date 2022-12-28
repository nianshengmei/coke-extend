package org.needcoke.coke.web.annotation;

import java.lang.annotation.*;


/**
 * 声明式http 客户端
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpClient {

    /**
     * 链接
     */
    String uri() default "";

    /**
     * 微服务服务名
     */
    String serviceName() default "";
}

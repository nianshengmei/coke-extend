package org.needcoke.coke.web.annotation;

import java.lang.annotation.*;


/**
 * 声明式http 客户端
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpClient {

    String value() default "";

    String name() default "";

    /**
     * 链接
     */
    String uri() default "";

    String path() default "";

    /**
     * 微服务服务名
     */
    String serviceName() default "";
}

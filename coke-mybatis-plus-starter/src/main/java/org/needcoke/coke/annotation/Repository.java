package org.needcoke.coke.annotation;

import java.lang.annotation.*;

/**
 *  用于适配持久层的注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Repository {

    String name() default "";

    String value() default "";
}

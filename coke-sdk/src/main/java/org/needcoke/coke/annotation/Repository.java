package org.needcoke.coke.annotation;

import pers.warren.ioc.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {

    String name() default "";

    String value() default "";
}

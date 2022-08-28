package org.needcoke.coke.annotation;

import pers.warren.ioc.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Service {

    String name() default "";

    String value() default "";
}

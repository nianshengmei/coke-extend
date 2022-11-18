package org.needcoke.coke.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AfterThrowing {

    String value() default "";

    String pointcut() default "";

    String throwing() default "";

    String argNames() default "";

    int order() default 0;
}

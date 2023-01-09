package org.needcoke.coke.kafka.annotation;

public @interface Producer {

    String topic() default "";

    String partition() default "";
}

package org.springframework.transaction.annotation;

public @interface Transactional {

    Class<?>[] rollbackFor() default {};
}

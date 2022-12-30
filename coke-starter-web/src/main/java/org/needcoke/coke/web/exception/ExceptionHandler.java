package org.needcoke.coke.web.exception;

import java.lang.annotation.*;


/**
 * @ExceptionHandler标注的方法有且最多有一个参数 即为异常本身
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {

    /**
     * 捕获的异常类型
     */
    Class<? extends Throwable>[] value() default {};
}

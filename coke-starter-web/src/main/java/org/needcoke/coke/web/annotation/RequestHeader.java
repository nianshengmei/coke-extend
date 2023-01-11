package org.needcoke.coke.web.annotation;

import java.lang.annotation.*;

/**
 * @author warren
 * @date 2022/4/2
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {

    String value() default "";

    String defaultValue() default "";
}

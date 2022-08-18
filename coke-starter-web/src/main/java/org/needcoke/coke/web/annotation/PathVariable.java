package org.needcoke.coke.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    String value() default "";

    /**
     *
     * GET /user/get/{userId}/must
     *
     * String[] ss = {"user","get","{userId}","must"}
     *
     * GET /user/get/S001/must
     *
     * String[] ss = {"user","get","S001","must"}
     */
}

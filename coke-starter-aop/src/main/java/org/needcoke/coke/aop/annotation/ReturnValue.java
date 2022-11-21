package org.needcoke.coke.aop.annotation;

import java.lang.annotation.*;

/**
 * 用于标志该参数希望coke容器注入标准请求的返回值
 * <p>
 * 仅用于AfterAdvice和AfterReturningAdvice
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReturnValue {
}

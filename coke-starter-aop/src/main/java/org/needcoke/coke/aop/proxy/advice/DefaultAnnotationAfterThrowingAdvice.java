package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import lombok.experimental.Accessors;
import java.lang.reflect.Method;

@Data
@Accessors(chain = true)
public class DefaultAnnotationAfterThrowingAdvice implements ThrowsAdvice{

    /**
     * 表达式
     */
    private String expression;

    /**
     * 方法
     */
    private Method method;
}

package org.needcoke.aop.proxy.advice;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

@Data
@Accessors(chain = true)
public class DefaultAnnotationAfterReturningAdvice implements AfterReturningAdvice{

    /**
     * 表达式
     */
    private String expression;

    /**
     * 方法
     */
    private Method method;


    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

    }
}

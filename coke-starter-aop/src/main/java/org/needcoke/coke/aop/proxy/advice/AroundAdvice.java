package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.aopalliance.intercept.Joinpoint;

import java.lang.reflect.Method;

/**
 * 环绕通知
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AroundAdvice extends CokeAdvice {

    /**
     * 方法
     */
    private Method method;

    /**
     * 的切点表达式
     */
    private String expression;

    /**
     * 切面的名称
     */
    private String aspectName;

    private Joinpoint joinpoint;

    public void invoke() throws Throwable{
        joinpoint.proceed();
    }

}

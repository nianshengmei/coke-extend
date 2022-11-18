package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.warren.ioc.core.Container;
import java.lang.reflect.Method;

/**
 * 抛出异常后通知
 *
 * @author warren
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ThrowsAdvice extends CokeAdvice {

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

    /**
     * 执行前置拦截
     *
     * @param beanMethod 非代理bean被切的方法
     * @param args       beanMethod的参数
     */
    public Object invoke(Method beanMethod, Object[] args,Throwable throwable)  throws Throwable {
        Object[] newArgs = getNewArgs(method, beanMethod, args, throwable,null);
        return method.invoke(Container.getContainer().getBean(aspectName), newArgs);
    }
}

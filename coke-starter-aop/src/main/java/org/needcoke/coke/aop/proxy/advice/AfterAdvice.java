package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.aop.exc.ProxyException;
import pers.warren.ioc.core.Container;

import java.lang.reflect.Method;

/**
 * finally后置通知
 *
 * @author warren
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Slf4j
public class AfterAdvice extends CokeAdvice {

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
    public void invoke(Method beanMethod, Object[] args, Object returnValue, Throwable throwable) throws Throwable {
        try {
            method.invoke(Container.getContainer().getBean(aspectName), getNewArgs(method, beanMethod, args, throwable, returnValue));
        } catch (Throwable e) {
            log.error("advice invoke error aspectName = {} , advice method name = {},args = {},return value = {} ,throwable = {}",
                    aspectName, method.getName(), args, returnValue, throwable);
            if (e instanceof NullPointerException) {
                throw new ProxyException("Note that if an exception has been encountered before the post notification, the return value is null, and the exception body is null if the normal execution", e);
            } else{
                throw e.getCause();
            }
        }
    }
}

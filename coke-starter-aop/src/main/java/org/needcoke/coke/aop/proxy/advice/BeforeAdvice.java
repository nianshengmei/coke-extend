package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 前置通知
 *
 * @author warren
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BeforeAdvice extends CokeAdvice {

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
    public void invoke(Method beanMethod, Object[] args) throws Throwable {
        method.invoke(Container.getContainer().getBean(aspectName), getNewArgs(method, beanMethod, args,null,null));
    }

}

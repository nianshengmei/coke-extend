package org.needcoke.aop.proxy;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.aspectj.weaver.tools.PointcutExpression;

import java.lang.reflect.Method;

@Accessors(chain = true)
public class DefaultAnnotationPointcut implements Pointcut {

    /**
     * 表达式
     */
    @Getter
    private String expression;

    private PointcutExpression pointcutExpression;

    /**
     * 方法
     */
    @Getter
    @Setter
    private Method method;


    public PointcutExpression getPointcutExpression() {
        return pointcutExpression;
    }

    public DefaultAnnotationPointcut setExpression(String expression) {
        this.expression = expression;
        this.pointcutExpression = new AspectJExpressionPointcut().buildPointcutExpression(expression,Thread.currentThread().getContextClassLoader());
        return this;
    }
}

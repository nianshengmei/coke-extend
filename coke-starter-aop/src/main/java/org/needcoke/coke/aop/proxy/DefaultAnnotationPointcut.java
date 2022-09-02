package org.needcoke.coke.aop.proxy;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.aspectj.weaver.tools.PointcutExpression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Accessors(chain = true)
public class DefaultAnnotationPointcut implements Pointcut {

    /**
     * 表达式
     */
    @Getter
    private String expression;

    /**
     * AspectJ切点表达式
     */
    private PointcutExpression pointcutExpression;

    /**
     * 方法
     */
    @Getter
    @Setter
    private Method method;

    private final List<ProxyConfig> proxyMethodList = new ArrayList<>();


    public PointcutExpression getPointcutExpression() {
        return pointcutExpression;
    }

    public DefaultAnnotationPointcut setExpression(String expression) {
        this.expression = expression;
        this.pointcutExpression = new AspectJExpressionPointcut().buildPointcutExpression(expression,Thread.currentThread().getContextClassLoader());
        return this;
    }

    @Override
    public Collection<ProxyConfig> getProxyConfigList() {
        return this.proxyMethodList;
    }
}

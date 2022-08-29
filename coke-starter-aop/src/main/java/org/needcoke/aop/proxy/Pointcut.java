package org.needcoke.aop.proxy;

import org.aspectj.weaver.tools.PointcutExpression;

public interface Pointcut {

    PointcutExpression getPointcutExpression();

    String getExpression();
}

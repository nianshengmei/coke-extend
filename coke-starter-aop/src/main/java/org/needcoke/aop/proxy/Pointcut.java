package org.needcoke.aop.proxy;

import org.aspectj.weaver.tools.PointcutExpression;
import java.util.Collection;

public interface Pointcut {

    PointcutExpression getPointcutExpression();

    String getExpression();

    Collection<ProxyConfig> getProxyConfigList();
}

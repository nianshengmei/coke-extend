package org.needcoke.coke.aop.proxy;

import org.aopalliance.aop.Advice;

public interface Advisor {

    Advice getAdvice();

    boolean isPerInstance();
}

package org.needcoke.aop.proxy;

import org.aopalliance.aop.Advice;

public interface Advisor {

    Advice getAdvice();

    boolean isPerInstance();
}

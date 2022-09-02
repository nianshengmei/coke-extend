package org.needcoke.coke.aop.proxy;

public interface TargetSource {

    Class<?> getTargetClass();

    boolean isStatic();

    Object getTarget() throws Exception;

    void releaseTarget(Object target) throws Exception;
}

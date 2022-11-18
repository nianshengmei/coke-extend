package org.needcoke.coke.aop.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * Cglib动态代理
 *
 * @author warren
 */
@Slf4j
public class CglibAopProxy extends AbstractAopProxy {

    public CglibAopProxy(Class<?> sourceBeanClz, String sourceBeanName) {
        super(sourceBeanClz, sourceBeanName);
        log.info("cglib 动态代理  {}", sourceBeanName);
    }

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}

package org.needcoke.coke.aop.core;

import org.needcoke.coke.aop.proxy.Aspect;
import pers.warren.ioc.core.ApplicationContext;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理容器上下文
 *
 * @author warren
 */
public class ProxyApplicationContext extends ApplicationContext {

    /**
     * key: 非代理对象bean的名称
     * <p>
     * value: 代理对象bean的名称
     */
    private final Map<String, String> proxyBeanNameMap = new HashMap<>();

    /**
     * 打了@Aspect注解的bean对象名称
     */
    private final Map<String, Aspect> aspectMap = new HashMap<>();

    /**
     * 存代理类配置
     *
     * @param name   @Aspect注解的bean的名称
     * @param aspect 代理配置
     */
    public void putAspect(String name, Aspect aspect) {
        aspectMap.put(name, aspect);
    }
}

package org.needcoke.aop.core;

import cn.hutool.core.collection.CollUtil;
import org.needcoke.aop.proxy.Aspect;
import org.needcoke.aop.proxy.ProxyConfig;
import pers.warren.ioc.core.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * 代理对象bean池
     *
     * <p>
     * key: 代理对象名称
     */
    private final Map<String, Object> proxyBeanMap = new HashMap<>();

    /**
     * 打了@Aspect注解的bean对象名称
     */
    private final Map<String, Aspect> aspectMap = new HashMap<>();

    /**
     * 存代理类配置
     *
     * @param name        @Aspect注解的bean的名称
     * @param aspect 代理配置
     */
    public void putAspect(String name, Aspect aspect) {
        aspectMap.put(name, aspect);
    }

    /**
     * 取代理类配置
     *
     * @param name @Aspect注解的bean的名称
     */
    public Aspect getAspect(String name) {
        return aspectMap.get(name);
    }

    public Stream<Aspect> getAspectStream(){
        return aspectMap.values().stream();
    }

    /**
     * 是否需要代理
     *
     * @param name 非代理对象的名称
     */
    public boolean isProxy(String name) {
        return proxyBeanMap.containsKey(name);
    }

    /**
     * 获取代理bean对象
     *
     * @param name 非代理对象的名称
     */
    public <T> T getProxyBean(String name) {
        return (T) proxyBeanMap.get(name);
    }

    /**
     * 获取代理bean对象
     *
     * @param clz bean的类型
     */
    public <T> T getProxyBean(Class<T> clz) {
        List<Object> beanList = proxyBeanMap.values().stream().filter(o -> o.getClass().equals(clz)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(beanList)) {
            return (T) beanList.get(0);
        }
        return null;
    }

    /**
     * 获取代理bean对象
     *
     * @param clz bean的类型
     */
    public <T> List<T> getProxyBeans(Class<T> clz) {
        return (List<T>) proxyBeanMap.values().stream().filter(o -> o.getClass().equals(clz)).collect(Collectors.toList());
    }
}

package org.needcoke.coke.mp.config;

import java.lang.reflect.Proxy;

/**
 * 代理对象创建工厂
 */
public class MapperProxyCreateFactory {

    /**
     * 通过接口的class创建该接口的代理对象(这里直接基于JDK提供的创建动态代理的工具来创建代理对象)
     *
     * @param mapperClass 接口的class
     * @return T 代理对象
     */
    public static <T> T getProxyService(Class<T> mapperClass) {
        // 该接口的Class对象是被那个类加载器加载的
        ClassLoader classLoader = mapperClass.getClassLoader();
        // 获取到该接口所有的interface
        Class<?>[] interfaces = {mapperClass};
        // jdk代理必须的handler，代理对象的方法执行就会调用这里的invoke方法。自动传入调用的方法 + 方法参数
        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler(mapperClass));
        // 返回代理对象
        return (T) proxy;
    }
}

package org.needcoke.aop.proxy;

import lombok.Data;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 代理配置
 *
 * @author warren
 */
@Data
public class ProxyConfig implements Serializable {

    /**
     * bean名称
     */
    private String beanName;

    /**
     * 被切到的方法集合
     */
    private Collection<Method> methodCollection = new ArrayList<>();

    /**
     * 获取bean名称
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 设置bean名称
     */
    public ProxyConfig setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    /**
     * 添加方法
     */
    public void addMethod(Method method) {
        this.methodCollection.add(method);
    }
}

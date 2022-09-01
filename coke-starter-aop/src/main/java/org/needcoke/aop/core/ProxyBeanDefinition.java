package org.needcoke.aop.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.warren.ioc.core.BeanDefinition;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProxyBeanDefinition extends BeanDefinition {



    /**
     * 原始beanDefinition的名称
     */
    protected String parentName;



}

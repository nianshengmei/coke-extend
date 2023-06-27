package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanFactory;
import pers.warren.ioc.core.FactoryBean;

public class MybatisPlusFactoryBean implements FactoryBean<BaseMapper> {


    /**
     * beanDefinition
     */
    private BeanDefinition beanDefinition;

    /**
     * 创建该FactoryBean的工厂
     */
    private BeanFactory currentBeanFactory;

    public MybatisPlusFactoryBean(BeanDefinition beanDefinition, BeanFactory currentBeanFactory) {
        this.beanDefinition = beanDefinition;
        this.currentBeanFactory = currentBeanFactory;
    }

    @Override
    public <T> T getObject() {
        return (T)MapperProxyCreateFactory.getProxyService(getType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<BaseMapper> getType() {
        return (Class<BaseMapper>) beanDefinition.getClz();
    }

    @Override
    public String getName() {
        return beanDefinition.getName();
    }

    @Override
    public Boolean isSingleton() {
        return true;
    }
}

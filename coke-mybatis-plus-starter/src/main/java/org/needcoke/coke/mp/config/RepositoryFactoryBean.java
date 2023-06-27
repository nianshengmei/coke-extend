package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanFactory;
import pers.warren.ioc.core.DefaultFactoryBean;

public class RepositoryFactoryBean extends DefaultFactoryBean {
    public RepositoryFactoryBean(BeanDefinition beanDefinition, BeanFactory currentBeanFactory) {
        super(beanDefinition, currentBeanFactory);
    }

    @Override
    public Object getObject() {
        GenericTypeUtils.setGenericTypeResolver(new MybatisPlusGenericTypeResolver());
        return super.getObject();
    }
}

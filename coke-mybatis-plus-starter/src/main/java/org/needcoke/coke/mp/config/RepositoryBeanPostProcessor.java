package org.needcoke.coke.mp.config;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.inject.InjectField;
import pers.warren.ioc.inject.InjectType;

import java.lang.reflect.Field;

public class RepositoryBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (IService.class.isAssignableFrom(beanDefinition.getClz())) {
            beanDefinition.setFactoryBeanClass(RepositoryFactoryBean.class);

            Field[] declaredFields = ReflectUtil.getFields(beanDefinition.getClz());
            for (Field declaredField : declaredFields) {
                if (declaredField.getName().equals("baseMapper")) {
                    beanDefinition.getAutowiredFieldInject().add(new InjectField(beanDefinition.getName(), declaredField, InjectType.BEAN));
                }
            }
        }
    }

}

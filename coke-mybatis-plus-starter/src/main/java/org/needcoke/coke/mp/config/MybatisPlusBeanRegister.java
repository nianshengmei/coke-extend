package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.needcoke.coke.annotation.Repository;
import pers.warren.ioc.core.*;
import pers.warren.ioc.enums.BeanType;
import pers.warren.ioc.util.ReflectUtil;
import pers.warren.ioc.util.StringUtil;

public class MybatisPlusBeanRegister extends AnnotationBeanRegister {


    @Override
    public String getName(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        return StringUtil.getOriginalName(importingClassMetadata.getSourceClass().getSimpleName());
    }

    @Override
    public BeanDefinition initialization(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class<?> sourceClass = importingClassMetadata.getSourceClass();
        if (BaseMapper.class.isAssignableFrom(sourceClass) && !BaseMapper.class.getTypeName().equals(sourceClass.getTypeName())) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(sourceClass, getName(importingClassMetadata, registry), BeanType.SIMPLE_BEAN, null, getName(importingClassMetadata, registry));
            builder.setBeanFactoryType(DefaultBeanFactory.class);
            builder.setFactoryBeanType(MybatisPlusFactoryBean.class);
            builder.setAnnotationMetadata(importingClassMetadata);
            builder.setRegister(this);
            BeanDefinition beanDefinition = builder.build();
            registry.registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            return beanDefinition;
        }

        if (ReflectUtil.containsAnnotation(sourceClass, Repository.class)) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(sourceClass, getName(importingClassMetadata, registry), BeanType.SIMPLE_BEAN, null, getName(importingClassMetadata, registry));
            builder.setBeanFactoryType(DefaultBeanFactory.class);
            builder.setFactoryBeanType(RepositoryFactoryBean.class);
            builder.setAnnotationMetadata(importingClassMetadata);
            builder.setRegister(this);
            BeanDefinition beanDefinition = builder.build();
            registry.registerBeanDefinition(beanDefinition.getName(), beanDefinition);
            return beanDefinition;
        }
        return null;
    }
}

package org.needcoke.coke.web.core;

import org.needcoke.coke.annotation.Controller;
import pers.warren.ioc.core.*;
import pers.warren.ioc.enums.BeanType;

public class WebBeanRegister extends AnnotationBeanRegister {
    @Override
    public String getName(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        String beanName = null;
        boolean hasController = metadata.hasAnnotation(Controller.class);
        if (hasController) {
            Controller controller = (Controller) metadata.getAnnotation(Controller.class);
            beanName = realBeanName(metadata, registry, controller.name(), controller.value());
        }
        return beanName;
    }

    @Override
    public BeanDefinition initialization(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = null;
        BeanDefinitionBuilder builder = null;
        if (metadata.hasAnnotation(Controller.class)) {
            String name = getName(metadata, registry);
            builder = BeanDefinitionBuilder.genericBeanDefinition(metadata.getSourceClass(),
                    name,
                    BeanType.COMPONENT,
                    null,
                    name
            );
            builder.setRegister(this);
            builder.setScanByAnnotationClass(Controller.class);
            builder.setScanByAnnotation(metadata.getAnnotation(Controller.class));
            beanDefinition = builder.build();
            registry.registerBeanDefinition(name,beanDefinition);
        }

        return beanDefinition;
    }
}

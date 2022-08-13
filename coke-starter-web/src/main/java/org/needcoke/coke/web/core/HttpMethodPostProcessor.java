package org.needcoke.coke.web.core;

import org.needcoke.coke.annotation.Controller;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;

public class HttpMethodPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (null != beanDefinition.getScanByAnnotation() && beanDefinition.getScanByAnnotation().equals(Controller.class)) {

        }
        BeanPostProcessor.super.postProcessBeforeInitialization(beanDefinition, register);
    }
}

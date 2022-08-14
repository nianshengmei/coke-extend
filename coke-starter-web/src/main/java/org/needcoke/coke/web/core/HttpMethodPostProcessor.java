package org.needcoke.coke.web.core;

import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.annotation.Controller;
import org.needcoke.coke.web.annotation.*;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class HttpMethodPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (null != beanDefinition.getScanByAnnotation() && beanDefinition.getScanByAnnotationClass().equals(Controller.class)) {
            Class<?> clz = beanDefinition.getClz();
            Controller controllerAnnotation = clz.getAnnotation(Controller.class);
            String[] mappings = controllerAnnotation.mapping();
            Method[] declaredMethods = clz.getDeclaredMethods();
            Container container = Container.getContainer();
            WebApplicationContext applicationContext = container.getBean(WebApplicationContext.class);
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getModifiers() == Modifier.PUBLIC) {

                    if (ReflectUtil.containsAnnotation(declaredMethod, GET.class)) {
                        GET getAnnotation = declaredMethod.getAnnotation(GET.class);
                        String value = getAnnotation.value();
                        for (String mapping : mappings) {
                            applicationContext.addWebFunction(HttpType.GET, getMapping(mapping, value), beanDefinition.getName(), declaredMethod);
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                            POST postAnnotation = declaredMethod.getAnnotation(POST.class);
                            String value = postAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.addWebFunction(HttpType.POST, getMapping(mapping, value), beanDefinition.getName(), declaredMethod);
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                            PUT putAnnotation = declaredMethod.getAnnotation(PUT.class);
                            String value = putAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.addWebFunction(HttpType.PUT, getMapping(mapping, value), beanDefinition.getName(), declaredMethod);
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                            DELETE deleteAnnotation = declaredMethod.getAnnotation(DELETE.class);
                            String value = deleteAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.addWebFunction(HttpType.DELETE, getMapping(mapping, value), beanDefinition.getName(), declaredMethod);
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                            PATCH patchAnnotation = declaredMethod.getAnnotation(PATCH.class);
                            String value = patchAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.addWebFunction(HttpType.PATCH, getMapping(mapping, value), beanDefinition.getName(), declaredMethod);
                            }
                        }
                    }
                }
            }
        }
        BeanPostProcessor.super.postProcessBeforeInitialization(beanDefinition, register);
    }

    private String getMapping(String clzMp, String methodMp) {
        if (!clzMp.startsWith("/") && StrUtil.isNotEmpty(clzMp)) {
            clzMp = "/" + clzMp;
        }

        if (!methodMp.startsWith("/") && StrUtil.isNotEmpty(methodMp)) {
            methodMp = "/" + methodMp;
        }
        return clzMp + methodMp;
    }

}

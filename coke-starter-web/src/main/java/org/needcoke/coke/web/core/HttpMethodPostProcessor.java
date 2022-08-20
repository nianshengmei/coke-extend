package org.needcoke.coke.web.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.annotation.Controller;
import org.needcoke.coke.web.annotation.*;
import org.needcoke.coke.web.http.Handler;
import org.needcoke.coke.web.http.HttpType;
import org.needcoke.coke.web.http.PathVariableRequestMappingHandler;
import org.needcoke.coke.web.http.RequestMappingHandler;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class HttpMethodPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        if (null != beanDefinition.getScanByAnnotation() && beanDefinition.getScanByAnnotationClass().equals(Controller.class)) {
            Class<?> clz = beanDefinition.getClz();
            Controller controllerAnnotation = clz.getAnnotation(Controller.class);
            String[] mappings = controllerAnnotation.mapping();
            Method[] declaredMethods = clz.getDeclaredMethods();
            TypeVariable<? extends Class<?>>[] typeParameters = clz.getTypeParameters();
            Container container = Container.getContainer();
            WebApplicationContext applicationContext = container.getBean(WebApplicationContext.class);
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getModifiers() == Modifier.PUBLIC) {

                    if (ReflectUtil.containsAnnotation(declaredMethod, GET.class)) {
                        GET getAnnotation = declaredMethod.getAnnotation(GET.class);
                        String value = getAnnotation.value();
                        String pathVariableValue = null;
                        if (value.contains("{") && value.contains("}")) {
                            List<Annotation> paramAnnotations = getParamAnnotations(declaredMethod, PathVariable.class);
                            if (CollUtil.isEmpty(paramAnnotations)) {
                                throw new RuntimeException(getMapping(mappings[0],value)+" must have @PathVariable annotation !");
                            }

                            if(CollUtil.size(paramAnnotations) > 1){
                                throw new RuntimeException("a request mapping must hava only PathVariable !  mapping = "+getMapping(mappings[0],value));
                            }
                            pathVariableValue = ((PathVariable)paramAnnotations.get(0)).value();
                        }
                        for (String mapping : mappings) {
                            applicationContext.putHandler(HttpType.GET.name() + " " + getMapping(mapping,value),newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,pathVariableValue));
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                            POST postAnnotation = declaredMethod.getAnnotation(POST.class);
                            String value = postAnnotation.value();
                            String pathVariableValue = null;
                            if (value.contains("{") && value.contains("}")) {
                                List<Annotation> paramAnnotations = getParamAnnotations(declaredMethod, PathVariable.class);
                                if (CollUtil.isEmpty(paramAnnotations)) {
                                    throw new RuntimeException(getMapping(mappings[0],value)+" must have @PathVariable annotation !");
                                }

                                if(CollUtil.size(paramAnnotations) > 1){
                                    throw new RuntimeException("a request mapping must hava only PathVariable !  mapping = "+getMapping(mappings[0],value));
                                }
                                pathVariableValue = ((PathVariable)paramAnnotations.get(0)).value();
                            }
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.POST.name() + " " + getMapping(mapping,value),newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,pathVariableValue));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                            PUT putAnnotation = declaredMethod.getAnnotation(PUT.class);
                            String value = putAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.PUT.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,null));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                            DELETE deleteAnnotation = declaredMethod.getAnnotation(DELETE.class);
                            String value = deleteAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.DELETE.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,null));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                            PATCH patchAnnotation = declaredMethod.getAnnotation(PATCH.class);
                            String value = patchAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.PATCH.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,null));
                            }
                        }
                    }
                }
            }
        }
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

    private List<Annotation> getParamAnnotations(Method method,Class annotationClz){
        List<Annotation> annotationList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            Annotation annotation = parameter.getAnnotation(annotationClz);
            if(null != annotation){
                annotationList.add(annotation);
            }
        }
        return annotationList;
    }

    private Handler newHandler(HttpType httpType,String invokeBeanName,Method invokeMethod,String pathVariableValue){
        RequestMappingHandler handler = null;
        if(StrUtil.isNotEmpty(pathVariableValue)){
            handler = new PathVariableRequestMappingHandler().setPathVariableValue(pathVariableValue);
        }else{
            handler = new RequestMappingHandler();
        }
        return handler.setHttpType(httpType).setInvokeMethod(invokeMethod).setInvokeBeanName(invokeBeanName);
    }

}

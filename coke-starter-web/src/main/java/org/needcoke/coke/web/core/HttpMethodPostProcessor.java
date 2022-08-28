package org.needcoke.coke.web.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.annotation.Controller;
import org.needcoke.coke.web.annotation.*;
import org.needcoke.coke.web.bean.ParamAnnotation;
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
            Container container = Container.getContainer();
            WebApplicationContext applicationContext = container.getBean(WebApplicationContext.class);
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getModifiers() == Modifier.PUBLIC) {

                    if (ReflectUtil.containsAnnotation(declaredMethod, GET.class)) {
                        GET getAnnotation = declaredMethod.getAnnotation(GET.class);
                        String value = getAnnotation.value();
                        String pathVariableValue = null;
                        String key = null;
                        if (value.contains("{") && value.contains("}")) {
                            List<ParamAnnotation> paramAnnotations = getParamAnnotations(declaredMethod, PathVariable.class);
                            if (CollUtil.isEmpty(paramAnnotations)) {
                                throw new RuntimeException(getMapping(mappings[0],value)+" must have @PathVariable annotation !");
                            }

                            if(CollUtil.size(paramAnnotations) > 1){
                                throw new RuntimeException("a request mapping must hava only PathVariable !  mapping = "+getMapping(mappings[0],value));
                            }
                            pathVariableValue= ((PathVariable)paramAnnotations.get(0).getAnnotation()).value();
                            pathVariableValue = StrUtil.isNotEmpty(pathVariableValue)?pathVariableValue:paramAnnotations.get(0).getParameterName();
                            key = paramAnnotations.get(0).getParameterName();
                        }
                        for (String mapping : mappings) {
                            applicationContext.putHandler(HttpType.GET.name() + " " + getMapping(mapping,value),newHandler(HttpType.GET,beanDefinition.getName(),declaredMethod,pathVariableValue,key,getMapping(mapping,value)));
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, POST.class)) {
                            POST postAnnotation = declaredMethod.getAnnotation(POST.class);
                            String value = postAnnotation.value();
                            String pathVariableValue = null;
                            String key = null;
                            if (value.contains("{") && value.contains("}")) {
                                List<ParamAnnotation> paramAnnotations = getParamAnnotations(declaredMethod, PathVariable.class);
                                if (CollUtil.isEmpty(paramAnnotations)) {
                                    throw new RuntimeException(getMapping(mappings[0],value)+" must have @PathVariable annotation !");
                                }

                                if(CollUtil.size(paramAnnotations) > 1){
                                    throw new RuntimeException("a request mapping must hava only PathVariable !  mapping = "+getMapping(mappings[0],value));
                                }
                                pathVariableValue= ((PathVariable)paramAnnotations.get(0).getAnnotation()).value();
                                pathVariableValue = StrUtil.isNotEmpty(pathVariableValue)?pathVariableValue:paramAnnotations.get(0).getParameterName();
                                key = paramAnnotations.get(0).getParameterName();
                            }
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.POST.name() + " " + getMapping(mapping,value),newHandler(HttpType.POST,beanDefinition.getName(),declaredMethod,pathVariableValue,key,getMapping(mapping,value)));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PUT.class)) {
                            PUT putAnnotation = declaredMethod.getAnnotation(PUT.class);
                            String value = putAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.PUT.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.PUT,beanDefinition.getName(),declaredMethod,null,null,null));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, DELETE.class)) {
                            DELETE deleteAnnotation = declaredMethod.getAnnotation(DELETE.class);
                            String value = deleteAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.DELETE.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.DELETE,beanDefinition.getName(),declaredMethod,null,null,null));
                            }
                        }
                    }

                    if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                        if (ReflectUtil.containsAnnotation(declaredMethod, PATCH.class)) {
                            PATCH patchAnnotation = declaredMethod.getAnnotation(PATCH.class);
                            String value = patchAnnotation.value();
                            for (String mapping : mappings) {
                                applicationContext.putHandler(HttpType.PATCH.name() + " " + getMapping(mapping,value)
                                        ,newHandler(HttpType.PATCH,beanDefinition.getName(),declaredMethod,null,null,null));
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

    private List<ParamAnnotation> getParamAnnotations(Method method, Class annotationClz){
        List<ParamAnnotation> annotationList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        for (int i = 0; i < parameters.length; i++) {
            Annotation annotation = parameters[i].getAnnotation(annotationClz);
            if(null != annotation){
                annotationList.add(new ParamAnnotation().setAnnotation(annotation).setParameter(parameters[i]).setParameterName(parameterNames[i]));
            }
        }
        return annotationList;
    }

    private Handler newHandler(HttpType httpType,String invokeBeanName,Method invokeMethod,String pathVariableValue,String key,String mapping){
        RequestMappingHandler handler = null;
        if(StrUtil.isNotEmpty(pathVariableValue)){
            mapping = mapping.replaceAll("\\{"+pathVariableValue+"}","*");
            String [] pathArr = mapping.split("/");
            handler = new PathVariableRequestMappingHandler().setKey(key).setPathArr(pathArr);
        }else{
            handler = new RequestMappingHandler();
        }
        return handler.setHttpType(httpType).setInvokeMethod(invokeMethod).setInvokeBeanName(invokeBeanName);
    }

    private boolean isPathVariable(String url){
        //TODO 正则判断规范
        if (url.contains("{") && url.contains("}")){
            return true;
        }
        return false;
    }
}

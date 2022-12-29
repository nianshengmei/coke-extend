package org.needcoke.coke.web.core;

import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.web.annotation.*;
import org.needcoke.coke.web.client.HttpClientCache;
import org.needcoke.coke.web.client.WebClientException;
import org.needcoke.coke.web.http.HttpType;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HttpClientProxy implements InvocationHandler, Serializable {

    private Class<?> clientInterface;


    public HttpClientProxy(Class<?> clientInterface) {
        this.clientInterface = clientInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }


    private void initCache(Class<?> clientInterface) {
        Method[] declaredMethods = clientInterface.getDeclaredMethods();
        HttpClient httpClientAnnotation = clientInterface.getAnnotation(HttpClient.class);
        String uri = httpClientAnnotation.uri();
        String path = httpClientAnnotation.path();
        String fullPath = StrUtil.removeSuffix(uri, "/");
        if (StrUtil.isNotEmpty(path)) {
            fullPath = fullPath + "/" + StrUtil.removePrefix(path, "/");
        }
        for (Method declaredMethod : declaredMethods) {
            int modify = validateAnnotation(declaredMethod);
            if (modify > 0) {
                handleHttpMethod(modify, fullPath, declaredMethod);
            }
        }
    }

    public int validateAnnotation(Method method) {
        int modify = 0;
        int i = 0;
        if (method.getAnnotation(GET.class) != null) {
            i++;
            modify = 1;
        }
        if (method.getAnnotation(POST.class) != null) {
            i++;
            modify = 2;
        }
        if (method.getAnnotation(PUT.class) != null) {
            i++;
            modify = 3;
        }
        if (method.getAnnotation(DELETE.class) != null) {
            i++;
            modify = 4;
        }
        if (method.getAnnotation(PATCH.class) != null) {
            i++;
            modify = 5;
        }

        if (i > 1) {
            String errorMsg = String.format("the method {%s} must have only one http annotation", method.getName());
            throw new WebClientException(errorMsg);
        }

        return modify;
    }

    public void handleHttpMethod(int modify, String fullPath, Method method) {
        HttpClientCache cache = new HttpClientCache();
        fullPath = StrUtil.removeSuffix(fullPath, "/");
        switch (modify) {
            case 1:
                GET getAnnotation = method.getAnnotation(GET.class);
                String pa = getAnnotation.value();
                if (StrUtil.isNotEmpty(pa)) {
                    fullPath = fullPath + "/" + StrUtil.removePrefix(pa, "/");
                }
                cache.setPath(fullPath);
                cache.setHttpType(HttpType.GET);
                break;
            case 2:
                POST postAnnotation = method.getAnnotation(POST.class);
                String pb = postAnnotation.value();
                if (StrUtil.isNotEmpty(pb)) {
                    fullPath = fullPath + "/" + StrUtil.removePrefix(pb, "/");
                }
                cache.setPath(fullPath);
                cache.setHttpType(HttpType.POST);
                break;
            case 3:
                PUT putAnnotation = method.getAnnotation(PUT.class);
                String pc = putAnnotation.value();
                if (StrUtil.isNotEmpty(pc)) {
                    fullPath = fullPath + "/" + StrUtil.removePrefix(pc, "/");
                }
                cache.setPath(fullPath);
                cache.setHttpType(HttpType.PUT);
                break;
            case 4:
                DELETE deleteAnnotation = method.getAnnotation(DELETE.class);
                String pd = deleteAnnotation.value();
                if (StrUtil.isNotEmpty(pd)) {
                    fullPath = fullPath + "/" + StrUtil.removePrefix(pd, "/");
                }
                cache.setPath(fullPath);
                cache.setHttpType(HttpType.DELETE);
                break;
            case 5:
                PATCH patchAnnotation = method.getAnnotation(PATCH.class);
                String pe = patchAnnotation.value();
                if (StrUtil.isNotEmpty(pe)) {
                    fullPath = fullPath + "/" + StrUtil.removePrefix(pe, "/");
                }
                cache.setPath(fullPath);
                cache.setHttpType(HttpType.PATCH);
                break;
            default:
                String errorMsg = String.format("not support http method  %s , %s", clientInterface.getTypeName(), method.getName());
                throw new WebClientException(errorMsg);
        }
        Parameter[] parameters = method.getParameters();
    }

}

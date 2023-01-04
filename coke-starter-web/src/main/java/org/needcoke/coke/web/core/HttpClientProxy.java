package org.needcoke.coke.web.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.needcoke.coke.web.annotation.*;
import org.needcoke.coke.web.client.*;
import org.needcoke.coke.web.http.HttpType;
import pers.warren.ioc.util.ReflectUtil;
import pers.warren.ioc.util.SerializeUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientProxy implements InvocationHandler, Serializable {

    private final Class<?> clientInterface;

    private static final OkHttpClientHolder holder = new OkHttpClientHolder();


    public HttpClientProxy(Class<?> clientInterface) {
        this.clientInterface = clientInterface;
        initCache(clientInterface);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpClientCache httpClientCache = HttpClientCacheMgmt.getInstance().get(clientInterface, method);
        String[] parameterNames = httpClientCache.getParameterNames();
        Map<String, Object> argsMap = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            argsMap.put(parameterNames[i], args[i]);
        }
        Request.Builder builder = new Request.Builder();

        okhttp3.RequestBody requestBody = null;
        if (StrUtil.isNotEmpty(httpClientCache.getBodyParamName())) {
            Object body = argsMap.get(httpClientCache.getBodyParamName());
            byte[] byteArray = new byte[0];
            if (null != body) {
                byteArray = SerializeUtil.toJson(body).getBytes(StandardCharsets.UTF_8);
            }
            requestBody = okhttp3.RequestBody.create(byteArray);
        }
        builder.method(httpClientCache.getHttpType().name(), requestBody);

        if (CollUtil.isNotEmpty(httpClientCache.getHeaderParamNameList())) {
            List<HttpMethodParamNameInfo> headerParamNameList = httpClientCache.getHeaderParamNameList();
            for (HttpMethodParamNameInfo httpMethodParamNameInfo : headerParamNameList) {
                Object header = argsMap.get(httpMethodParamNameInfo.getMethodParameterName());
                if (null == header && StrUtil.isNotEmpty(httpMethodParamNameInfo.getDefaultValue().toString())) {
                    header = httpMethodParamNameInfo.getDefaultValue();
                }
                builder.addHeader(httpMethodParamNameInfo.getCastToName(), String.valueOf(header));
            }

        }

        StringBuilder urlParamBuilder = new StringBuilder();
        if (CollUtil.isNotEmpty(httpClientCache.getUrlParamNameList())) {
            List<HttpMethodParamNameInfo> urlParamNameList = httpClientCache.getUrlParamNameList();
            for (HttpMethodParamNameInfo httpMethodParamNameInfo : urlParamNameList) {
                Object param = argsMap.get(httpMethodParamNameInfo.getMethodParameterName());
                if (null == param && StrUtil.isNotEmpty(httpMethodParamNameInfo.getDefaultValue().toString())) {
                    param = httpMethodParamNameInfo.getDefaultValue();
                }
                urlParamBuilder.append("&")
                        .append(httpMethodParamNameInfo.getCastToName())
                        .append("=");
                if (ObjectUtil.isNotEmpty(param)) {
                    urlParamBuilder.append(param);
                } else {
                    urlParamBuilder.append("null");
                }
            }
            urlParamBuilder.replace(0, 1, "?");
        }
        builder.url(httpClientCache.getPath() + urlParamBuilder);
        Request request = builder.build();
        Call call = holder.getOkHttpClient().newCall(request);
        Response response;
        try {
            response = call.execute();
        } catch (Throwable e) {
            throw new WebNetException(e);
        }
        if (!response.isSuccessful() || null == response.body()) {
            throw new WebNetException(String.format("网络异常 %s",response.message()));
        }
        ResponseBody body = response.body();
        String json = body.string();
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            return SerializeUtil.fromJson(json, httpClientCache.getReturnType());
        } catch (Throwable e) {
            throw new WebNetException(json);
        }
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
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        List<HttpMethodParamNameInfo> paramMap = new ArrayList<>();
        List<HttpMethodParamNameInfo> headerMap = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(RequestBody.class) != null) {
                cache.setBodyParamName(parameterNames[i]);
                continue;
            }

            if (parameters[i].getAnnotation(RequestHeader.class) != null) {
                RequestHeader annotation = parameters[i].getAnnotation(RequestHeader.class);
                HttpMethodParamNameInfo httpMethodParamNameInfo = new HttpMethodParamNameInfo()
                        .setMethodParameterName(parameterNames[i])
                        .setDefaultValue(annotation.defaultValue())
                        .setCastToName(annotation.value());
                headerMap.add(httpMethodParamNameInfo);
                continue;
            }

            HttpMethodParamNameInfo httpMethodParamNameInfo = new HttpMethodParamNameInfo()
                    .setMethodParameterName(parameterNames[i]);
            if (parameters[i].getAnnotation(RequestParam.class) != null) {
                RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
                httpMethodParamNameInfo.setDefaultValue(annotation.defaultValue())
                        .setCastToName(annotation.value());
            } else {
                httpMethodParamNameInfo.setDefaultValue("")
                        .setCastToName(parameterNames[i]);
            }
            if (StrUtil.isEmpty(httpMethodParamNameInfo.getCastToName())) {
                httpMethodParamNameInfo.setCastToName(parameterNames[i]);
            }
            paramMap.add(httpMethodParamNameInfo);
        }
        cache.setHeaderParamNameList(headerMap);
        cache.setUrlParamNameList(paramMap);
        cache.setParameterNames(parameterNames);
        cache.setReturnType(method.getReturnType());
        HttpClientCacheMgmt.getInstance().addCache(clientInterface, method, cache);
    }

}

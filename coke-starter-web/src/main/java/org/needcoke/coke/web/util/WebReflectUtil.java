package org.needcoke.coke.web.util;

import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.web.annotation.PathVariable;
import org.needcoke.coke.web.annotation.RequestHeader;
import org.needcoke.coke.web.annotation.RequestParam;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 专属于web的反射模块
 *
 * <p>其中一些独属于web的注解</p>
 *
 * @author warren
 * @since 1.0.3
 */
public class WebReflectUtil {

    /**
     * 获取方法的参数名称
     *
     * @param method 方法
     * @author warren
     * @since 1.0.3
     */
    public static String[] getParameterNames(final Method method) {
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        if (null != parameters) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                if (null != requestParam && StrUtil.isNotEmpty(requestParam.value())) {
                    parameterNames[i] = requestParam.value();
                    continue;
                }

                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                if (null != pathVariable && StrUtil.isNotEmpty(pathVariable.value())) {
                    parameterNames[i] = pathVariable.value();
                    continue;
                }

                RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
                if (null != requestHeader && StrUtil.isNotEmpty(requestHeader.value())) {
                    parameterNames[i] = requestHeader.value();
                }
            }
        }
        return parameterNames;
    }

    /**
     * 获取构造函数的参数名称
     *
     * @param constructor 构造函数
     * @author warren
     * @since 1.0.3
     */
    public static String[] getParameterNames(final Constructor<?> constructor) {
        return ReflectUtil.getParameterNames(constructor).toArray(new String[0]);
    }


}

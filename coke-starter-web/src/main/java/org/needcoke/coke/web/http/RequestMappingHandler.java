package org.needcoke.coke.web.http;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.annotation.RequestParam;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.core.ApplicationContext;
import pers.warren.ioc.util.ReflectUtil;

/**
 * @author warren
 * @date 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Slf4j
public class RequestMappingHandler extends AbstractHandler{

    private String invokeBeanName;

    private Method invokeMethod;

    private HttpType httpType;

    @Override
    public void handle(HttpContext ctx, ApplicationContext applicationContext) throws Throwable{
//        Object bean = applicationContext.getBean(invokeBeanName);
//        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
//        String[] parameterNames = getParameterNames(invokeMethod);
//        Map<String, String[]> paramMap = ctx.paramMap();
//        Object[] parameters = getParams(parameterTypes, parameterNames, paramMap);
        Object result = invokeMethod.invoke(getBean(applicationContext), getParameters(ctx));
        ctx.writeJson(result);
    }


    private Object[] getParams(Class<?>[] parameterTypes, String[] parameterNames, Map<String, String[]> paramMap) {
        Object[] parameters = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String[] parameterArr = paramMap.get(parameterNames[i]);
            if (null != parameterArr && parameterArr.length > 0) {
                String param = parameterArr[0];
                Class<?> parameterType = parameterTypes[i];
                Object dstValue = ParameterUtil.getDstValue(param, parameterType);
                parameters[i] = dstValue;
            }
        }
        return parameters;
    }

    protected String[] getParameterNames(Method method){
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if(null != requestParam && StrUtil.isNotEmpty(requestParam.value())){
                parameterNames[i] = requestParam.value();
            }
        }
        return parameterNames;
    }

    public Object getBean(ApplicationContext applicationContext){
        return applicationContext.getBean(invokeBeanName);
    }

    public Object[] getParameters(HttpContext ctx){
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        String[] parameterNames = getParameterNames(invokeMethod);
        Map<String, String[]> paramMap = ctx.getParamMap();
        return getParams(parameterTypes, parameterNames, paramMap);
    }
}

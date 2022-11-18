package org.needcoke.coke.web.http;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.annotation.RequestBody;
import org.needcoke.coke.web.annotation.RequestParam;
import org.needcoke.coke.web.bean.RequestParamItem;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.core.ApplicationContext;
import pers.warren.ioc.util.ReflectUtil;

/**
 * @author warren
 * @date 2022/4/2
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Slf4j
public class RequestMappingHandler extends AbstractHandler {

    private RequestParamItem[] paramItemArr;

    @Setter
    @Getter
    private String invokeBeanName;

    @Getter
    private Method invokeMethod;

    @Setter
    @Getter
    private HttpType httpType;

    private String bodyName;

    private Class bodyClz;

    private Class[] innerTypes;


    private int bodyIndex = -1;

    @Setter
    @Getter
    private HttpContext ctx;

    @Override
    public void handle(HttpContext ctx, ApplicationContext applicationContext) throws Throwable {
        this.ctx = ctx;
        Object[] parameters = getParameters(ctx);
        Object result = invokeMethod.invoke(getBean(applicationContext), parameters);
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
        if (null != paramItemArr && paramItemArr.length > 0) {

            for (int i = 0; i < paramItemArr.length; i++) {
                if (null == paramItemArr[i]) {
                    continue;
                }
                int paramIndex = paramItemArr[i].getParamIndex();
                if (-1 == paramIndex) continue;
                if (parameters[paramIndex] == null) {
                    parameters[paramIndex] = paramItemArr[i].getDefaultValue();
                    if (paramItemArr[i].isRequired() && parameters[paramIndex] == null) {
                        throw new RuntimeException("bean " + invokeBeanName + " 's method " + invokeMethod.getName() + " 's parameter " + paramItemArr[i].getName() + " must have !");
                    }
                }
            }
        }
        if (-1 != bodyIndex && null == parameters[bodyIndex]) {
            String body = "";
            try {
                body = ctx.body();
                parameters[bodyIndex] = JSONUtil.toBean(body, bodyClz);
            } catch (Exception e) {
                try {
                    if (bodyClz.isAssignableFrom(List.class)) {
                        System.out.println(body);
                    }
                    parameters[bodyIndex] = JSONUtil.toList(body, Object.class);
                } catch (Exception a) {
                    throw new RuntimeException("body parse error bean " + invokeBeanName + " 's method " + invokeMethod.getName() + " 's parameter " + bodyName, a);

                }
            }
        }
        return parameters;
    }

    protected String[] getParameterNames(Method method) {
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (null != requestParam && StrUtil.isNotEmpty(requestParam.value())) {
                parameterNames[i] = requestParam.value();
            }
        }
        return parameterNames;
    }

    public Object getBean(ApplicationContext applicationContext) {
        return applicationContext.getBean(invokeBeanName);
    }

    public Object[] getParameters(HttpContext ctx) {
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        String[] parameterNames = getParameterNames(invokeMethod);
        Map<String, String[]> paramMap = ctx.getParamMap();
        return getParams(parameterTypes, parameterNames, paramMap);
    }

    public RequestMappingHandler setInvokeMethod(Method invokeMethod) {
        this.invokeMethod = invokeMethod;
        initRequestParamItemArr(invokeMethod);
        initBodyField(invokeMethod);
        return this;
    }

    protected void initBodyField(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        for (int i = 0; i < parameters.length; i++) {
            if (null != parameters[i].getAnnotation(RequestBody.class)) {
                bodyClz = parameters[i].getType();
                bodyName = parameterNames[i];
                bodyIndex = i;
            }
        }
    }

    protected void initRequestParamItemArr(Method method) {
        Parameter[] parameters = method.getParameters();
        List<Parameter> requestParamList = Arrays.stream(parameters)
                .filter(p -> null == p.getAnnotation(RequestBody.class)
                ).collect(Collectors.toList());
        String[] parameterNames = ReflectUtil.getParameterNames(method);
        Map<Parameter, String> mp = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            mp.put(parameters[i], parameterNames[i]);
        }
        this.paramItemArr = new RequestParamItem[requestParamList.size()];
        for (int i = 0; i < requestParamList.size(); i++) {
            Parameter parameter = requestParamList.get(i);
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
//            if (null == annotation) return;
            this.paramItemArr[i] = new RequestParamItem()
                    .setParamIndex(i)
                    .setName(mp.get(parameter))
                    .setRequestParamName(null == annotation ? null : annotation.value())
                    .setRequired(null != annotation && annotation.required())
                    .setRequestParam(null != annotation)
                    .setDefaultValue(null == annotation || StrUtil.isEmpty(annotation.defaultValue()) ? null : ParameterUtil.getDstValue(annotation.defaultValue(), parameter.getType()));
        }
    }


}

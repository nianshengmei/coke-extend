package org.needcoke.coke.web.http;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.core.ApplicationContext;
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PathVariableRequestMappingHandler extends RequestMappingHandler {
    //是BeanPostProcessor时填的
    private String key;

    private String value;

    //是BeanPostProcessor时填的
    private String[] pathArr;

    public boolean match(String[] httpRequestPathArr) {
        if (null == pathArr || null == httpRequestPathArr || pathArr.length != httpRequestPathArr.length) {
            return false;
        }
        String tmpValue = "";
        for (int i = 0; i < httpRequestPathArr.length; i++) {
            if (pathArr[i].equals("*")) {
                tmpValue = httpRequestPathArr[i];
            } else {
                if (!pathArr[i].equals(httpRequestPathArr[i])) {
                    return false;
                }
            }
        }
        value = tmpValue;
        return true;
    }

    @Override
    public void handle(HttpContext ctx, ApplicationContext applicationContext) throws Throwable {
        setCtx(ctx);
        Object[] parameters = getParameters(ctx);
        String[] parameterNames = getParameterNames(getInvokeMethod());
        Class<?>[] parameterTypes = getInvokeMethod().getParameterTypes();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(key)) {
                parameters[i] = ParameterUtil.getDstValue(value,parameterTypes[i]);
            }
        }
        Object result = getInvokeMethod().invoke(getBean(applicationContext), parameters);
        ctx.writeJson(result);
    }


}

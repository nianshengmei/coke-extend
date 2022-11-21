package org.needcoke.coke.aop.proxy.advice;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.needcoke.coke.aop.annotation.ReturnValue;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class CokeAdvice implements Advice {

    public abstract Method getMethod();

    public Object[] getNewArgs(Method method, Method beanMethod, Object[] args, Throwable throwable, Object returnValue) {
        Class<?>[] adviceMethodParameterTypes = method.getParameterTypes();
        String[] adviceMethodParameterNames = ReflectUtil.getParameterNames(method);
        String[] beanMethodParameterNames = ReflectUtil.getParameterNames(beanMethod);
        Parameter[] adviceMethodParameters = method.getParameters();
        Map<String, Object> beanMethodParamMap = new HashMap<>();
        for (int i = 0; i < beanMethodParameterNames.length; i++) {
            beanMethodParamMap.put(beanMethodParameterNames[i], args[i]);
        }
        Object[] newArgs = new Object[adviceMethodParameterNames.length];
        boolean outFlag = true;
        for (int i = 0; i < adviceMethodParameterNames.length; i++) {
            boolean flag = true;
            if (beanMethodParamMap.containsKey(adviceMethodParameterNames[i])) {
                Object arg = beanMethodParamMap.get(adviceMethodParameterNames[i]);
                if (arg.getClass().getTypeName().equals(adviceMethodParameterTypes[i].getTypeName())) {
                    newArgs[i] = arg;
                    flag = false;
                    outFlag = false;
                }
            }

            if (null != throwable && adviceMethodParameterTypes[i].isAssignableFrom(throwable.getClass()) && flag && outFlag) {
                newArgs[i] = throwable;
            }

            if (null != returnValue && null != adviceMethodParameters[i].getAnnotation(ReturnValue.class)) {
                newArgs[i] = returnValue;
            }
        }
        return newArgs;
    }


}

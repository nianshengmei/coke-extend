package org.needcoke.coke.aop.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MethodUtil {

    private static final Map<Integer, String> methodNameMap = new HashMap<>();


    public String getMethodName(Method method) {
        if (methodNameMap.containsKey(method.hashCode())) {
            return methodNameMap.get(method.hashCode());
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getMethodAccess(method.getModifiers()))
                .append(" ")
                .append(method.getReturnType().getName())
                .append(" ")
                .append(method.getName())
                .append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            builder.append(parameterType.getTypeName())
                    .append(" ");
        }
        builder.append(")");
        String name = builder.toString();
        methodNameMap.put(method.hashCode(), name);
        return name;
    }

    public String getMethodAccess(Integer modifiers) {
        String ret = "";
        if (Modifier.isPublic(modifiers)) {
            ret += "public";
        }

        if (Modifier.isPrivate(modifiers)) {
            ret += "private";
        }

        if (Modifier.isProtected(modifiers)) {
            ret += "protected";
        }

        if (Modifier.isStatic(modifiers)) {
            ret += " static";
        }

        return ret;
    }

}

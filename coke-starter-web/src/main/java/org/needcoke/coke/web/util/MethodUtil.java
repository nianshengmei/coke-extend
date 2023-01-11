package org.needcoke.coke.web.util;

import java.lang.reflect.Method;

public class MethodUtil {

    private static final String ALARM_SIGNAL = "#";

    private static final String LEFT_BRACKET = "(";

    private static final String RIGHT_BRACKET = ")";

    private static final String COMMA = ",";

    //{modifyNumber}#{returnType typeName}#{methodName}({...parameterType typeName})
    public static String getOnlyName(Method method){
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(method.getModifiers())
                .append(ALARM_SIGNAL)
                .append(method.getReturnType().getTypeName())
                .append(ALARM_SIGNAL)
                .append(method.getName())
                .append(LEFT_BRACKET);
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            nameBuilder.append(parameterTypes[i].getTypeName());
            if(i != parameterTypes.length -1) {
                nameBuilder.append(COMMA);
            }
        }
        nameBuilder.append(RIGHT_BRACKET);
        return nameBuilder.toString();
    }
}

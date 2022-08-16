package org.needcoke.coke.web.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class ParameterUtil {

    public Object getDstValue(String value, Class clz) {
        String clzName = clz.getName();
        if (Integer.class.getTypeName().equals(clzName)) {
            return Integer.parseInt(value);
        } else if (int.class.getTypeName().equals(clzName)) {
            return Integer.parseInt(value);
        } else if (String.class.getTypeName().equals(clzName)) {
            return value;
        } else if (Long.class.getTypeName().equals(clzName)) {
            return Long.parseLong(value);
        } else if (long.class.getTypeName().equals(clzName)) {
            return Long.parseLong(value);
        } else if (Double.class.getTypeName().equals(clzName)) {
            return Double.parseDouble(value);
        } else if (double.class.getTypeName().equals(clzName)) {
            return Double.parseDouble(value);
        } else if (Short.class.getTypeName().equals(clzName)) {
            return Short.parseShort(value);
        } else if (short.class.getTypeName().equals(clzName)) {
            return Short.parseShort(value);
        } else if (Boolean.class.getTypeName().equals(clzName)) {
            return Boolean.parseBoolean(value);
        } else if (boolean.class.getTypeName().equals(clzName)) {
            return Boolean.parseBoolean(value);
        } else if (Byte.class.getTypeName().equals(clzName)) {
            return Byte.parseByte(value);
        } else if (byte.class.getTypeName().equals(clzName)) {
            return Byte.parseByte(value);
        } else if (Character.class.getTypeName().equals(clzName)) {
            return StrUtil.isNotEmpty(value) ? value.charAt(0) : null;
        } else if (char.class.getTypeName().equals(clzName)) {
            return StrUtil.isNotEmpty(value) ? value.charAt(0) : null;
        } else if (Float.class.getTypeName().equals(clzName)) {
            return Float.parseFloat(value);
        } else if (float.class.getTypeName().equals(clzName)) {
            return Float.parseFloat(value);
        } else if (Date.class.getTypeName().equals(clzName)) {
            return DateUtil.parse(value, DatePattern.NORM_DATETIME_FORMATTER);
        } else if (String[].class.getTypeName().equals(clzName)) {
            value = trim(value);
            String[] ss = value.split(",");
            for (int i = 0; i < ss.length; i++) {
                ss[i] = trim(ss[i]);
            }
            return ss;
        } else if (Integer[].class.getTypeName().equals(clzName) || int[].class.getTypeName().equals(clzName)) {
            value = trim(value);
            String[] ss = value.split(",");
            Integer[] is = new Integer[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ss[i] = trim(ss[i]);
                is[i] = Integer.parseInt(ss[i]);
            }
            return ss;
        } else if (Double[].class.getTypeName().equals(clzName) || double[].class.getTypeName().equals(clzName)) {
            value = trim(value);
            String[] ss = value.split(",");
            Double[] is = new Double[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ss[i] = trim(ss[i]);
                is[i] = Double.parseDouble(ss[i]);
            }
            return ss;
        } else if (Long[].class.getTypeName().equals(clzName) || long[].class.getTypeName().equals(clzName)) {
            value = trim(value);
            String[] ss = value.split(",");
            Long[] is = new Long[ss.length];
            for (int i = 0; i < ss.length; i++) {
                ss[i] = trim(ss[i]);
                is[i] = Long.parseLong(ss[i]);
            }
            return ss;
        } else {
            throw new RuntimeException("类型不支持 ！class =" + clz.getTypeName());
        }

    }

    private String trim(String value) {
        if (value.length() > 2) {
            if ((value.startsWith("'") && value.endsWith("'")) ||
                    (value.startsWith("\"") && value.endsWith("\""))) {
                value = value.substring(1, value.length() - 1);
            }
        }
        return value;
    }


}

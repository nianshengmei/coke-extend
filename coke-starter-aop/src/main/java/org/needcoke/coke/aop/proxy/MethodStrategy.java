package org.needcoke.coke.aop.proxy;

import lombok.Data;

import java.util.function.Function;

/**
 * 方法策略
 */
@Data
public class MethodStrategy {

    private Class<?> methodAnnotation;

    private AdviceType type;

    private String aspectName;

    public MethodStrategy(Class<?> methodAnnotation, String aspectName) {
        this.methodAnnotation = methodAnnotation;
        this.type = AdviceType.AFTER;
        this.aspectName = aspectName;

    }

    public MethodStrategy(Class<?> methodAnnotation, AdviceType type, String aspectName, String functionName) {
        this.methodAnnotation = methodAnnotation;
        this.type = type;
        this.aspectName = aspectName;
    }
}

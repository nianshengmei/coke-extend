package org.needcoke.coke.web.exception;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

@Data
@Accessors(chain = true)
public class HandlerCache {

    /**
     * 异常的类型名称
     */
    private Class<? extends Throwable> exceptionType;

    private Method handleMethod;

    private boolean insertThrowable;

    private String adviceName;



}

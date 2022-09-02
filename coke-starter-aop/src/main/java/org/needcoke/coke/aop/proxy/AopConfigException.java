package org.needcoke.coke.aop.proxy;

import org.aopalliance.aop.AspectException;

public class AopConfigException extends AspectException {

    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}

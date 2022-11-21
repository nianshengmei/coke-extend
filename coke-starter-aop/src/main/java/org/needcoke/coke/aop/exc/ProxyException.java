package org.needcoke.coke.aop.exc;

public class ProxyException extends RuntimeException{

    public ProxyException() {
        super();
    }

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}

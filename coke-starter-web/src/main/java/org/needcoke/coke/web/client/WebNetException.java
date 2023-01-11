package org.needcoke.coke.web.client;

public class WebNetException extends RuntimeException{

    public WebNetException(String message) {
        super(message);
    }

    public WebNetException(Throwable cause) {
        super(cause);
    }
}

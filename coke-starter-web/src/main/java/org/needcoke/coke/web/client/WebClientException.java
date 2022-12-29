package org.needcoke.coke.web.client;

public class WebClientException  extends RuntimeException {

    public WebClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebClientException(String message) {
        super(message);
    }
}
package org.needcoke.coke.http;


public class ThrowsNotifyObject {

    private Throwable throwable;

    private Object data;

    public ThrowsNotifyObject(Throwable throwable, Object data) {
        this.throwable = throwable;
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Object getData() {
        return data;
    }
}

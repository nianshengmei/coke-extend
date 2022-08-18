package org.needcoke.coke.web.http;

public interface CokeHttpHandler {

    default void run(CokeHttpContext context) {
        try {
            preRun(context);

            realRun(context);

            postRun(context);

            complete(context);

        } catch (Throwable e) {
            //全局异常处理
        }
    }

    default void preRun(CokeHttpContext context) throws Exception {
    }

    int realRun(CokeHttpContext context) throws Exception;

    default void postRun(CokeHttpContext context) throws Exception {
    }

    default void complete(CokeHttpContext context) throws Exception {
    }
}

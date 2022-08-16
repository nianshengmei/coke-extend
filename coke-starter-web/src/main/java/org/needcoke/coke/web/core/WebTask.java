package org.needcoke.coke.web.core;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author warren
 */
@Slf4j
public abstract class WebTask implements Runnable {

    protected HttpType httpType;

    protected String requestUri;

    protected Map<String, String[]> parameterMap;

    protected HttpServletResponse httpServletResponse;

    public WebTask(HttpType httpType, String requestUri, Map<String, String[]> parameterMap,
                   HttpServletResponse resp) {
        this.httpType = httpType;
        this.requestUri = requestUri;
        this.parameterMap = parameterMap;
        this.httpServletResponse = resp;
    }

    @Override
    public void run() {
        preRun();

        realRun();

        postRun();

        complete();
    }

    public void preRun() {
    }

    public void realRun() {
    }

    public void postRun() {
    }

    public void complete() {
    }

}

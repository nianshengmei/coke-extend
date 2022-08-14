package org.needcoke.coke.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author warren
 */
public abstract class WebTask implements Runnable{

    protected HttpServletRequest httpServletRequest;

    protected HttpServletResponse httpServletResponse;

    public WebTask(HttpServletRequest req, HttpServletResponse resp) {
        this.httpServletRequest = req;
        this.httpServletResponse = resp;
    }

    @Override
    public void run() {
        preRun();

        realRun();

        postRun();

        complete();
    }

    public void preRun(){}

    public void realRun(){}

    public void postRun(){}

    public void complete(){}


}

package org.needcoke.coke.web.core;

import lombok.extern.slf4j.Slf4j;
/**
 * @author warren
 */
@Slf4j
public abstract class WebTask implements Runnable {
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

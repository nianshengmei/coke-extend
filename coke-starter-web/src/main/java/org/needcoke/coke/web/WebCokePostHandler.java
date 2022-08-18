package org.needcoke.coke.web;

import lombok.RequiredArgsConstructor;
import org.needcoke.coke.http.WebServer;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.handler.CokePostHandler;

@Component
@RequiredArgsConstructor
public class WebCokePostHandler implements CokePostHandler{

    private final WebServer webServer;

    @Override
    public void run() {
       webServer.start();
    }


}

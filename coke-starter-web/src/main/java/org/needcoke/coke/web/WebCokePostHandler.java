package org.needcoke.coke.web;

import org.apache.catalina.startup.Tomcat;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.handler.CokePostHandler;

@Component
public class WebCokePostHandler implements CokePostHandler {

    @Override
    public void run() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.setHostname("127.0.0.1");
        tomcat.setBaseDir("."); // tomcat 信息保存在项目下
    }
}

package org.needcoke.coke.web;

import cn.hutool.core.util.StrUtil;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import pers.warren.ioc.CokeApplication;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.annotation.Value;
import pers.warren.ioc.handler.CokePostHandler;

@Component
public class WebCokePostHandler extends CokeApplication implements CokePostHandler{

    private Tomcat tomcat;

    @Value("server.port:8080")
    private int port;

    @Value("server.hostname:'127.0.0.1'")
    private String host;

    @Value("server.session.timeout:100")
    private int session_timeout;

    @Override
    public void run() {
        loadContext();
        tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(port);
        if(StrUtil.isNotEmpty(host)) {
            tomcat.setHostname(host);
        }

        //context configuration start 开始上下文相关配置
        Context context = stepContext();

        //**************session time setting start Session时间相关*****************
        if (session_timeout > 0) {
            context.setSessionTimeout(session_timeout);
        }
        //**************session time setting end*****************
        context.setAllowCasualMultipartParsing(true);
        tomcat.getConnector();
        try {
            tomcat.start();
        }catch (Exception e){
            throw new RuntimeException("tomcat start failed in port = "+port+" !");
        }
        new Thread(()->{
            tomcat.getServer().await();
        }).start();
    }

    protected Context stepContext(){
        //1.初始化上下文
        Context context = tomcat.addContext("", null);//第二个参数与文档相关
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        //2.添加 servlet
        String servletName = "coke";
        tomcat.addServlet(context, servletName, new CokeMainServlet());

        //3.建立 servlet 印射
        context.addServletMappingDecoded("/", servletName);//Servlet与对应uri映射

        return context;
    }
}

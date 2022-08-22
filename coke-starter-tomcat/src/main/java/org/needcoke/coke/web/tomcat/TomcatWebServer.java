package org.needcoke.coke.web.tomcat;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.needcoke.coke.http.CokeHttpServlet;
import org.needcoke.coke.http.CokeWebConfig;
import org.needcoke.coke.http.WebServer;
import org.needcoke.coke.http.WebServerException;
import pers.warren.ioc.annotation.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TomcatWebServer implements WebServer {

    @Getter
    private Tomcat tomcat;

    private final CokeWebConfig webConfiguration;

    private final CokeHttpServlet cokeMainServlet;


    @Override
    public void start() throws WebServerException {
        try {
            initialize();
            tomcat.start();
            log.info("Tomcat initialized with port(s): " + getPort());
        }catch (Exception e){
            throw new WebServerException("Unable to start embedded Tomcat", e);
        }
        new Thread(()-> tomcat.getServer().await()).start();
    }

    @Override
    public void stop() throws WebServerException {
        try {
            tomcat.stop();
        }catch (Exception e){
            throw new WebServerException(e.getMessage(),e);
        }
    }

    @Override
    public int getPort() {
        Connector connector = this.tomcat.getConnector();
        if (connector != null) {
            return connector.getLocalPort();
        }
        return 0;
    }

    private void initialize() throws WebServerException{
        tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(webConfiguration.getServerPort());
        if(StrUtil.isNotEmpty(webConfiguration.getHostName())) {
            tomcat.setHostname(webConfiguration.getHostName());
        }

        //context configuration start 开始上下文相关配置
        Context context = stepContext();

        //**************session time setting start Session时间相关*****************
        if (webConfiguration.getSession_timeout() > 0) {
            context.setSessionTimeout(webConfiguration.getSession_timeout());
        }
        //**************session time setting end*****************
        context.setAllowCasualMultipartParsing(true);
        tomcat.getConnector();

    }

    protected Context stepContext(){
        //1.初始化上下文
        Context context = tomcat.addContext("", null);//第二个参数与文档相关
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        //2.添加 servlet
        String servletName = "coke";
        tomcat.addServlet(context, servletName, cokeMainServlet);

        //3.建立 servlet 印射
        context.addServletMappingDecoded("/", servletName);//Servlet与对应uri映射

        return context;
    }
}

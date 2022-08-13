package org.needcoke.coke.web.tomcat;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.needcoke.coke.web.config.CokeWebConfiguration;
import org.needcoke.coke.web.core.WebServer;
import org.needcoke.coke.web.core.WebServerException;

@Slf4j
public class TomcatWebServer implements WebServer {

    @Getter
    private Tomcat tomcat;

    private CokeWebConfiguration webConfiguration;

    public TomcatWebServer(Tomcat tomcat, CokeWebConfiguration webConfiguration) {
        this.tomcat = tomcat;
        this.webConfiguration = webConfiguration;
    }

    @Override
    public void start() throws WebServerException {
        try {
            tomcat.start();
        }catch (Exception e){
            throw new WebServerException("Unable to start embedded Tomcat", e);
        }
    }

    @Override
    public void stop() throws WebServerException {

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
        log.info("Tomcat initialized with port(s): " + getPort());
    }
}

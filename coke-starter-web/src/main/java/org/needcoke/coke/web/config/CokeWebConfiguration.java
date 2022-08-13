package org.needcoke.coke.web.config;

import lombok.Data;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;

@Data
@Configuration
public class CokeWebConfiguration {

    @Value("server.port:8080")
    private int serverPort;


    private String hostName;

}

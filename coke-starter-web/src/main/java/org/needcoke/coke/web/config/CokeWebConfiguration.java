package org.needcoke.coke.web.config;

import lombok.Data;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;

@Data
@Configuration
public class CokeWebConfiguration {


    @Value("server.port:8080")
    private int serverPort;

    @Value("server.hostname:'127.0.0.1'")
    private String hostName;

    @Value("server.session.timeout:60000")
    private int session_timeout;

    @Value("server.request.character.encoding:'UTF-8'")
    private String requestCharacterEncoding;

    @Value("server.response.character.encoding:'UTF-8'")
    private String responseCharacterEncoding;

}

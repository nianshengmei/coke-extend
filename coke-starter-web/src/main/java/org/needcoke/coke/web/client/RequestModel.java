package org.needcoke.coke.web.client;


import lombok.Data;
import org.needcoke.coke.web.http.HttpType;

import java.util.Map;

@Data
public class RequestModel {

    /**
     * 请求类型
     */
    private HttpType httpType;

    private String uri;

    private boolean serviceName;

    private String path;

    private Object body;

    private Map<String,Object> urlParam;

    private Map<String,String> headerMap;

}

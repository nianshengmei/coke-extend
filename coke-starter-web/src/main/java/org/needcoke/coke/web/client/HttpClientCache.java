package org.needcoke.coke.web.client;

import lombok.Data;
import org.needcoke.coke.web.http.HttpType;

import java.util.List;

@Data
public class HttpClientCache {

     private HttpType httpType;

     private String path;

    private String bodyParamName;

    private List<HttpMethodParamNameInfo> urlParamNameList;

    private List<HttpMethodParamNameInfo> headerParamNameList;

    private String[] parameterNames;

    private Class<?> returnType;
}

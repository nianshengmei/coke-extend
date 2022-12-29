package org.needcoke.coke.web.client;

import lombok.Data;
import org.needcoke.coke.web.http.HttpType;

@Data
public class HttpClientCache {

     private HttpType httpType;

     private String path;

    private String bodyParamName;

    private HttpMethodParamNameInfo[] urlParamNameArray;

    private HttpMethodParamNameInfo[] headerParamNameArray;
}

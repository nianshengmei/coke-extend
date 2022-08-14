package org.needcoke.coke.web.http;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.needcoke.coke.web.core.ContentType;
import org.needcoke.coke.web.core.HttpType;

import java.io.InputStream;
import java.util.Map;

/**
 * @author warren
 * @date 2022/4/2
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class CokeHttpRequest {

    private HttpType httpType;

    private ContentType contentType;

    private InputStream bodyInputStream;

    private Map<String,String> paramMap;

    private Map<String,String> headerMap;

    private String authType;
}
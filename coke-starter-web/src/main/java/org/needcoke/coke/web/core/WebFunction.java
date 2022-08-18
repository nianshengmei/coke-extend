package org.needcoke.coke.web.core;

import lombok.Data;
import lombok.experimental.Accessors;
import org.needcoke.coke.web.http.HttpType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author warren
 * @date 2022/4/2
 */
@Data
@Accessors(chain = true)
public class WebFunction {

    private HttpType httpType;

    private String invokeBeanName;

    private Method invokeMethod;

    private boolean body;

    Map<String,String> defaultValueMap;

    List<String> reqParam;

    List<String> mustReqParam;
}

package org.needcoke.coke.web.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HttpMethodParamNameInfo {

    /**
     * 通过反射读取的参数名
     */
    private String methodParameterName;

    /**
     * 发出http请求时发出的名称
     */
    private String castToName;

    /**
     * 未找到值时给出的默认值
     */
    private Object defaultValue;
}

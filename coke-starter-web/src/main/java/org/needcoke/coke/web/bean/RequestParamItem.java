package org.needcoke.coke.web.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestParamItem {

    private String name;

    private int paramIndex = -1;

    private String requestParamName;

    private boolean requestParam = false;

    private boolean required = false;

    private Object defaultValue;
}

package org.needcoke.coke.web.http;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PathVariableRequestMappingHandler extends RequestMappingHandler {

    private String pathVariableValue;
}

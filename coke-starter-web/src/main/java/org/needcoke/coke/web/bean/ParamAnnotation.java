package org.needcoke.coke.web.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@Data
@Accessors(chain = true)
public class ParamAnnotation {

    private Annotation annotation;

    private Parameter parameter;

    private String parameterName;
}

package org.needcoke.coke.web.core;

import pers.warren.ioc.core.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author warren
 * @date 2022/4/2
 */
public class WebApplicationContext extends ApplicationContext {


    private final Map<String,WebFunction> httpFunctionMap = new HashMap<>();
}

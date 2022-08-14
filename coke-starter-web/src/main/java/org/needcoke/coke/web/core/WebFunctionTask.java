package org.needcoke.coke.web.core;

import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author warren
 */
public class WebFunctionTask extends WebTask {

    private final WebFunction webFunction;

    public WebFunctionTask(WebFunction function, HttpServletRequest req, HttpServletResponse resp) {
        super(req, resp);
        this.webFunction = function;
    }

    @Override
    public void preRun() {

    }

    @Override
    public void realRun() {
        Method invokeMethod = webFunction.getInvokeMethod();
        String beanName = webFunction.getInvokeBeanName();
        Container container = Container.getContainer();
        Object bean = container.getBean(beanName);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        String[] parameterNames = ReflectUtil.getParameterNames(invokeMethod);

        System.out.println("hello");
    }
}

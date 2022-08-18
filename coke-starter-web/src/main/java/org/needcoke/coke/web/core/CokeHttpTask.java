package org.needcoke.coke.web.core;

import com.google.gson.Gson;
import org.needcoke.coke.web.http.CokeHttpContext;
import org.needcoke.coke.web.http.CokeHttpContextHolder;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CokeHttpTask extends WebTask{

    private CokeHttpContext httpContext;

    public CokeHttpTask() {
    }

    public CokeHttpTask(CokeHttpContext context) {
        this.httpContext = context;
    }

    public CokeHttpTask(HttpServletRequest req, HttpServletResponse resp, WebApplicationContext applicationContext){
        this.httpContext = new CokeHttpContext(req,resp,applicationContext);
    }

    @Override
    public void realRun() {
        httpContext = CokeHttpContextHolder.getContext();
        WebFunction webFunction = httpContext.getWebFunction();
        if(null == webFunction){

        } else{
            Method invokeMethod = webFunction.getInvokeMethod();
            String beanName = webFunction.getInvokeBeanName();
            Container container = Container.getContainer();
            Object bean = container.getBean(beanName);
            Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
            String[] parameterNames = ReflectUtil.getParameterNames(invokeMethod);

            Map<String, String[]> paramMap = httpContext.paramMap();
            Object[] parameters = getParams(parameterTypes,parameterNames,paramMap);
            Method md = webFunction.getInvokeMethod();
            try {
                Object invoke = md.invoke(bean, parameters);
                httpContext.writeJson(invoke);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private Object[] getParams(Class<?>[] parameterTypes,String[] parameterNames,Map<String, String[]> paramMap){
        Object[] parameters = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String[] parameterArr = paramMap.get(parameterNames[i]);
            if (null != parameterArr && parameterArr.length > 0) {
                String param = parameterArr[0];
                Class<?> parameterType = parameterTypes[i];
                Object dstValue = ParameterUtil.getDstValue(param, parameterType);
                parameters[i] = dstValue;
            }
        }
        return parameters;
    }


}

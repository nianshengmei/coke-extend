package org.needcoke.coke.web.http;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.needcoke.coke.web.core.WebFunction;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.util.ReflectUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class HttpMainHandler implements CokeHttpHandler {
    @Override
    public int realRun(CokeHttpContext context) throws Exception {
        if (null == context.getWebFunction()) {
            return noMatchUri(context);
        }
        return doRun(context);
    }


    private int noMatchUri(CokeHttpContext context) throws Exception {
        String uri = context.getUri();
        if (uri.equals("GET /favicon.ico")) {
            return favicon(context);
        } else {
            return uri_500(context);
        }

    }


    private int favicon(CokeHttpContext context) throws Exception {
        HttpServletResponse response = context.getResponse();
        response.setContentType("image/x-icon");
        response.setCharacterEncoding("UTF-8");
        InputStream stream = ResourceUtil.getStream("favicon.ico");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(IoUtil.readBytes(stream));
        outputStream.flush();
        return 0;
    }

    private int uri_500(CokeHttpContext context) throws Exception{
        String html = ResourceUtil.readUtf8Str("ERROR-PAGE/" + 500 + ".html");
        html = html.replaceAll("\r", "").replaceAll("\n", "");
        context.getResponse().setContentType("text/html;charset=utf-8");
        context.getResponse().setCharacterEncoding("UTF-8");
        context.getResponse().getWriter().write(html);
        return 0;
    }

    private int doRun(CokeHttpContext context) throws Exception {
        WebFunction webFunction = context.getWebFunction();
        Method invokeMethod = webFunction.getInvokeMethod();
        String beanName = webFunction.getInvokeBeanName();
        Object bean = context.getApplicationContext().getBean(beanName);
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        String[] parameterNames = ReflectUtil.getParameterNames(invokeMethod);
        Map<String, String[]> paramMap = context.paramMap();
        Object[] parameters = getParams(parameterTypes, parameterNames, paramMap);
        Method md = webFunction.getInvokeMethod();
        Object invoke = md.invoke(bean, parameters);
        context.writeJson(invoke);
        return 0;
    }

    private Object[] getParams(Class<?>[] parameterTypes, String[] parameterNames, Map<String, String[]> paramMap) {
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

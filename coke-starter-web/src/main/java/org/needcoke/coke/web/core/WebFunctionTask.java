package org.needcoke.coke.web.core;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.util.ParameterUtil;
import pers.warren.ioc.core.Container;
import pers.warren.ioc.util.ReflectUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author warren
 */
@Slf4j
public class WebFunctionTask extends WebTask {

    private final WebApplicationContext applicationContext = Container.getContainer().getBean(WebApplicationContext.class);

    public WebFunctionTask(HttpType httpType, String requestUri, Map<String, String[]> parameterMap,
                           HttpServletResponse resp) {
        super(httpType, requestUri, parameterMap, resp);
    }

    @Override
    public void preRun() {

    }

    @Override
    public void realRun() {
        log.info("httpType = {} , requestUri = {}", httpType.name(), requestUri);
        if (requestUri.equals("/favicon.ico") && httpType == HttpType.GET) {
            try {
                favicon();
            }catch (Exception e){
                throw new RuntimeException();
            }
            return;
        }
        WebFunction webFunction = applicationContext.getWebFunction(httpType, requestUri);
        if (null == webFunction) {
            try {
                errorPage("500");
            } catch (Exception e) {
                throw new RuntimeException();
            }
            return;
        }
        Method invokeMethod = webFunction.getInvokeMethod();
        String beanName = webFunction.getInvokeBeanName();
        Container container = Container.getContainer();
        Object bean = container.getBean(beanName);
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        String[] parameterNames = ReflectUtil.getParameterNames(invokeMethod);
        Object[] parameters = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String[] parameterArr = parameterMap.get(parameterNames[i]);
            if (null != parameterArr && parameterArr.length > 0) {
                String param = parameterArr[0];
                Class<?> parameterType = parameterTypes[i];
                Object dstValue = ParameterUtil.getDstValue(param, parameterType);
                parameters[i] = dstValue;
            }
        }
        Method md = webFunction.getInvokeMethod();
        try {
            Object invoke = md.invoke(bean, parameters);
            String json = new Gson().toJson(invoke);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void favicon() throws IOException {
        httpServletResponse.setContentType("image/x-icon");
        httpServletResponse.setCharacterEncoding("UTF-8");
        InputStream stream = ResourceUtil.getStream("favicon.ico");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(IoUtil.readBytes(stream));
        outputStream.flush();
    }

    private void errorPage(String errorCode) throws Exception {
        String html = ResourceUtil.readUtf8Str("ERROR-PAGE/" + errorCode + ".html");
        html = html.replaceAll("\r", "").replaceAll("\n", "");
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(html);
    }


}

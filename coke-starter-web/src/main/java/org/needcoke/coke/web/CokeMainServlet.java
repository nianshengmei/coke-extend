package org.needcoke.coke.web;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.core.*;
import org.needcoke.coke.web.http.CokeHttpRequest;
import org.needcoke.coke.web.util.IOUtil;
import pers.warren.ioc.core.Container;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author warren
 */
@Slf4j
public class CokeMainServlet extends HttpServlet {

    private final WebApplicationContext applicationContext = Container.getContainer().getBean(WebApplicationContext.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String contentType = req.getContentType();
        String body = IOUtil.getBody(req);
        Map<String, String[]> parameterMap = req.getParameterMap();
        Enumeration<String> headerNames = req.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        String authType = req.getAuthType();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headerMap.put(name, req.getHeader(name));
        }

        String method = req.getMethod();

        if (requestURI.equals("/favicon.ico")) {
            resp.setContentType("image/x-icon");
            InputStream stream = ResourceUtil.getStream("favicon.ico");
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(IoUtil.readBytes(stream));
            outputStream.flush();
            return;
        }
        log.info("请求进来了 httpType = {} , uri = {}", method, requestURI);

        WebFunction webFunction = applicationContext.getWebFunction(HttpType.valueOf(method), requestURI);
        if (null != webFunction) {
            CokeHttpThreadPool.getCoreThreadPool().newTask(new WebFunctionTask(webFunction, req, resp));
        } else {
            resp.setStatus(500);
            resp.getWriter().print("<html>" +
                    "<title>500</title>" +
                    "<body><font size = '60px' style='color:red;'>没有找到对应得资源!<font></body>" +
                    "<html>" +
                    "");
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
        }


    }
}
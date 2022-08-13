package org.needcoke.coke.web;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.util.IOUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author warren
 */
@Slf4j
public class CokeMainServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String contentType = req.getContentType();
        String read = IOUtil.getBody(req);
        Map<String, String[]> parameterMap = req.getParameterMap();
        Enumeration<String> headerNames = req.getHeaderNames();
        Map<String,String> headerMap = new HashMap<>();
        String authType = req.getAuthType();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headerMap.put(name,req.getHeader(name));
        }

        String method = req.getMethod();
        log.info("请求进来了 uri = {}", requestURI);
        resp.getWriter().print(read + "\n"+contentType + "\n"+new Gson().toJson(parameterMap)+ "\n"+new Gson().toJson(headerMap)

                + "\n"+ authType+ "\n"+ method
        );
    }


}

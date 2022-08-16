package org.needcoke.coke.web;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.core.*;
import pers.warren.ioc.core.Container;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author warren
 */
@Slf4j
public class CokeMainServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpType httpType = HttpType.valueOf(req.getMethod());
        String requestURI = req.getRequestURI();
        CokeHttpThreadPool.getCoreThreadPool().newTask(new WebFunctionTask(httpType, requestURI,
                req.getParameterMap(),
                resp));
    }


}

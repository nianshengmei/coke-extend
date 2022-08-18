package org.needcoke.coke.web;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.http.CokeHttpServlet;
import org.needcoke.coke.web.core.*;
import org.needcoke.coke.web.http.CokeHttpContext;
import org.needcoke.coke.web.http.CokeHttpContextHolder;
import pers.warren.ioc.annotation.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author warren
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CokeMainServlet extends CokeHttpServlet {

    private final CokeHttpThreadPool httpThreadPool;

    private final WebApplicationContext webApplicationContext;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CokeHttpContext cokeHttpContext = new CokeHttpContext(req, resp, webApplicationContext);
        CokeHttpContextHolder.setContext(cokeHttpContext);
        httpThreadPool.newTask(new CokeHttpTask());
    }


}

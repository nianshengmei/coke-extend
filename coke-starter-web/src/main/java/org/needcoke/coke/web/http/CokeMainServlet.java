package org.needcoke.coke.web.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.http.CokeHttpServlet;
import org.needcoke.coke.web.core.*;
import pers.warren.ioc.annotation.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author warren
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CokeMainServlet extends CokeHttpServlet {

    private final WebApplicationContext webApplicationContext;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CokeHttpHandler httpHandler = webApplicationContext.getBean(CokeHttpHandler.class);
        if (null != httpHandler) {
            httpHandler.run(new CokeHttpContext(req, resp, webApplicationContext));
        }
    }

}

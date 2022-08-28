package org.needcoke.coke.web.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.core.Order;
import org.needcoke.coke.http.CokeHttpServlet;
import org.needcoke.coke.web.core.WebApplicationContext;
import pers.warren.ioc.annotation.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * @author warren
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CokeMainServlet extends CokeHttpServlet {

    private final WebApplicationContext applicationContext;

    private List<HandlerMapping> handlerMappingList;

    public void autowiredHandlerMappingList(){
        handlerMappingList = applicationContext.getBeans(HandlerMapping.class);
        handlerMappingList.sort(Comparator.comparingInt(Order::getOrder));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(null == handlerMappingList) autowiredHandlerMappingList();
        for (HandlerMapping handlerMapping : handlerMappingList) {
            if (handlerMapping.mapping(req,resp)) {
                break;
            }
        }
    }

}

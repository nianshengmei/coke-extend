package org.needcoke.coke.web.http;

import lombok.RequiredArgsConstructor;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class DefaultHandlerAdapter implements HandlerAdapter{

    private final ApplicationContext applicationContext;

    @Override
    public void handle(HttpServletRequest request , HttpServletResponse response,Handler handler) {
        try {
            handler.handle(request, response, applicationContext);
        }catch (Throwable e){
           response.setStatus(502);
           throw new RuntimeException(e);
        }
    }
}

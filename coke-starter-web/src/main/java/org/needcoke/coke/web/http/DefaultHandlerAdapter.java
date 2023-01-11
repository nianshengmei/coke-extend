package org.needcoke.coke.web.http;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.web.exception.HandlerCache;
import org.needcoke.coke.web.exception.HandlerCacheMgmt;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultHandlerAdapter implements HandlerAdapter{

    private final ApplicationContext applicationContext;

    @Override
    public void handle(HttpServletRequest request , HttpServletResponse response,Handler handler) {
        try {
            handler.handle(request, response, applicationContext);
        }catch (Throwable e){
            Class<? extends Throwable> expClz = e.getCause().getClass();
            if (HandlerCacheMgmt.instance.contains(expClz)) {
                HandlerCache handlerCache = HandlerCacheMgmt.instance.get(expClz);
                Object bean = applicationContext.getBean(handlerCache.getAdviceName());
                if(handlerCache.isInsertThrowable()){
                    try {
                        Object o = handlerCache.getHandleMethod().invoke(bean,e.getCause());
                        writeJson(response,o);
                        return;
                    } catch (Throwable ex) {
                        log.error("coke web 异常捕获,快照 {}",handlerCache);
                        throw new RuntimeException(ex);
                    }
                }else{
                    try {
                        Object o = handlerCache.getHandleMethod().invoke(bean);
                        writeJson(response,o);
                        return;
                    } catch (Throwable ex) {
                        log.error("coke web 异常捕获,快照 {}",handlerCache);
                        throw new RuntimeException(ex);
                    }
                }
            }
            response.setStatus(502);
           throw new RuntimeException(e);
        }
    }

    public void writeJson(HttpServletResponse response,Object o) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (null == o) {
            response.getWriter().flush();
        } else {
            response.getWriter().write(JSONUtil.toJsonStr(o));
        }
    }
}

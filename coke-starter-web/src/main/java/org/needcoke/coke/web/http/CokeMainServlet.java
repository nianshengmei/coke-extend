package org.needcoke.coke.web.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.needcoke.coke.core.Order;
import org.needcoke.coke.http.CokeHttpServlet;
import org.needcoke.coke.http.WebServerException;
import org.needcoke.coke.web.core.WebApplicationContext;
import org.needcoke.coke.web.interceptor.Interceptor;
import org.needcoke.coke.web.interceptor.InterceptorCacheMgmt;
import org.needcoke.coke.web.util.AntPathMatcher;
import pers.warren.ioc.annotation.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author warren
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CokeMainServlet extends CokeHttpServlet {

    private final WebApplicationContext applicationContext;

    private List<HandlerMapping> handlerMappingList;

    public void autowiredHandlerMappingList() {
        handlerMappingList = applicationContext.getBeans(HandlerMapping.class);
        handlerMappingList.sort(Comparator.comparingInt(Order::getOrder));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!preHandle(req, resp)) {
            return;
        }
        if (null == handlerMappingList) autowiredHandlerMappingList();
        for (HandlerMapping handlerMapping : handlerMappingList) {
            if (handlerMapping.mapping(req, resp)) {
                afterCompletion(req, resp);
                return;
            }
        }
        throw new WebServerException(String.format("no match url path %s", req.getRequestURI()));
    }

    private void afterCompletion(HttpServletRequest req, HttpServletResponse resp) {
        Set<String> patternSet = InterceptorCacheMgmt.instance.keySet();
        for (String pattern : patternSet) {
            if (matcher.match(pattern, req.getRequestURI())) {
                List<String> interceptorNameList = InterceptorCacheMgmt.instance.get(pattern);
                for (String interceptorName : interceptorNameList) {
                    Interceptor interceptor = applicationContext.getBean(interceptorName);
                    interceptor.afterCompletion(req, resp);
                }
            }
        }
    }

    private final AntPathMatcher matcher = new AntPathMatcher();

    private boolean preHandle(HttpServletRequest req, HttpServletResponse resp) {
        Set<String> patternSet = InterceptorCacheMgmt.instance.keySet();
        for (String pattern : patternSet) {
            if (matcher.match(pattern, req.getRequestURI())) {
                List<String> interceptorNameList = InterceptorCacheMgmt.instance.get(pattern);
                for (String interceptorName : interceptorNameList) {
                    Interceptor interceptor = applicationContext.getBean(interceptorName);
                    if (!interceptor.preHandle(req, resp)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}

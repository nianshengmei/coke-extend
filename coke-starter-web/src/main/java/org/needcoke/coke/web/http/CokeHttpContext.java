package org.needcoke.coke.web.http;

import com.google.gson.Gson;
import lombok.Data;
import org.needcoke.coke.web.core.HttpType;
import org.needcoke.coke.web.core.WebApplicationContext;
import org.needcoke.coke.web.core.WebFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Data
public class CokeHttpContext {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final WebApplicationContext applicationContext;

    private final Gson gson = new Gson();


    public CokeHttpContext(HttpServletRequest request, HttpServletResponse response, WebApplicationContext applicationContext) {
        this.request = request;
        this.response = response;
        this.applicationContext = applicationContext;
    }

    public String getUri() {
        synchronized (CokeHttpContext.class) {
            return request.getMethod() + " " + request.getRequestURI();
        }
    }

    public HttpType getHttpType(){
        synchronized (CokeHttpContext.class) {
            return HttpType.valueOf(request.getMethod());
        }
    }

    public WebFunction getWebFunction(){
        synchronized (CokeHttpContext.class) {
            return applicationContext.getWebFunction(getUri());
        }
    }



    public Map<String,String[]> paramMap(){
        synchronized (CokeHttpContext.class) {
            return request.getParameterMap();
        }
    }

    public void writeJson(Object o) throws IOException {
        synchronized (CokeHttpContext.class) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(o));
        }
    }


}

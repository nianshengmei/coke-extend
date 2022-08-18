package org.needcoke.coke.web.http;

import com.google.gson.Gson;
import lombok.Data;
import org.needcoke.coke.web.core.WebApplicationContext;
import org.needcoke.coke.web.core.WebFunction;
import org.needcoke.coke.web.util.IOUtil;

import javax.servlet.ServletInputStream;
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
        return request.getMethod() + " " + request.getRequestURI();
    }

    public HttpType getHttpType() {
        return HttpType.valueOf(request.getMethod());
    }

    public WebFunction getWebFunction() {
        return applicationContext.getWebFunction(getUri());
    }


    public Map<String, String[]> paramMap() {
        return request.getParameterMap();
    }

    public String body() throws IOException {
        return IOUtil.getBody(request);
    }

    //简单对象得
    public <T> T fromJson(String jsonString,Class<T> tClass){
        return gson.fromJson(jsonString,tClass);
    }

    public void writeJson(Object o) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(o));

    }
}

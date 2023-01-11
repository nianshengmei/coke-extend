package org.needcoke.coke.web.http;

import org.needcoke.coke.web.interceptor.Interceptor;
import pers.warren.ioc.annotation.Component;
import pers.warren.ioc.core.ApplicationContext;

import java.lang.reflect.Constructor;
import java.util.List;

@Component
public class WebMvcConfigurationSupport {

    private final ApplicationContext applicationContext;

    private final List<Interceptor> interceptors;

    public WebMvcConfigurationSupport(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.interceptors = applicationContext.getBeans(Interceptor.class);
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void addInterceptor(Class<?> clz) throws Exception {
        if (clz.isAssignableFrom(Interceptor.class)) {
            Constructor<?> constructor = clz.getConstructor();
            Interceptor interceptor = (Interceptor) constructor.newInstance();
            interceptors.add(interceptor);
        } else {
            throw new RuntimeException("clz must implements Interceptor interface !");
        }
    }

    public List<Interceptor> getInterceptors(){
        return interceptors;
    }

}

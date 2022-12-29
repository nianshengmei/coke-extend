package org.needcoke.coke.web.core;

import org.needcoke.coke.http.WebServerException;
import org.needcoke.coke.web.annotation.*;
import org.needcoke.coke.web.client.WebClientException;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpClientProxy implements InvocationHandler, Serializable {

    private Class<?> clientInterface;


    public HttpClientProxy(Class<?> clientInterface) {
        this.clientInterface = clientInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }


    private void initCache(Class<?> clientInterface) {
        Method[] declaredMethods = clientInterface.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            validateAnnotation(declaredMethod);
        }
    }

    public boolean validateAnnotation(Method method) {
        int i = 0;
        if (method.getAnnotation(GET.class) != null) {
            i++;
        }
        if (method.getAnnotation(POST.class) != null) {
            i++;
        }
        if (method.getAnnotation(PUT.class) != null) {
            i++;
        }
        if (method.getAnnotation(DELETE.class) != null) {
            i++;
        }
        if (method.getAnnotation(PATCH.class) != null) {
            i++;
        }

        if(i > 1){
            String errorMsg = String.format("the method {%s} must have only one http annotation", method.getName());
            throw new WebClientException(errorMsg);
        }

        return i != 0;
    }

}

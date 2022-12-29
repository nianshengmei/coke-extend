package org.needcoke.coke.web.core;

import org.needcoke.coke.web.client.RequestModel;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpClientProxy implements InvocationHandler, Serializable {


    public HttpClientProxy(RequestModel requestModel) {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}

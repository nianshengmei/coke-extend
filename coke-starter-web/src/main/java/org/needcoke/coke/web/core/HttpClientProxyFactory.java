package org.needcoke.coke.web.core;

import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class HttpClientProxyFactory {

    public <Client> Client getProxyHttpClient(Class<Client> clientClass) {
        return (Client) Proxy.newProxyInstance(
                ClassUtil.getClassLoader(),
                new Class[]{clientClass},
                new HttpClientProxy(clientClass)
        );
    }
}

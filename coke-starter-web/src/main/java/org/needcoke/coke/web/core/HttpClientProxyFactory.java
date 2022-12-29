package org.needcoke.coke.web.core;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.needcoke.coke.web.annotation.HttpClient;
import org.needcoke.coke.web.client.RequestModel;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class HttpClientProxyFactory {

    public <Client> Client getProxyHttpClient(Class<Client> clientClass) {
        HttpClient httpClientAnnotation = clientClass.getAnnotation(HttpClient.class);
        String uri = httpClientAnnotation.uri();
        String serviceName = httpClientAnnotation.serviceName();
        RequestModel requestModel = new RequestModel();
        if(StrUtil.isNotEmpty(uri)){
            //外部调用
            requestModel.setUri(uri);
            requestModel.setServiceName(false);
        }
        if(StrUtil.isNotEmpty(serviceName)){
            //TODO 跨服务调用
        }
        return (Client) Proxy.newProxyInstance(ClassUtil.getClassLoader(), new Class[]{clientClass}, new HttpClientProxy(requestModel));
    }
}

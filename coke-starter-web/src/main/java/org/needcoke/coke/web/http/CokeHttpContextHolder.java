package org.needcoke.coke.web.http;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CokeHttpContextHolder {


    private final ThreadLocal<CokeHttpContext> THREAD_LOCAL = new TransmittableThreadLocal<>();


    public void setContext(CokeHttpContext context){
        THREAD_LOCAL.set(context);
    }

    public CokeHttpContext getContext(){
        return THREAD_LOCAL.get();
    }

    public void clear() {
        THREAD_LOCAL.remove();
    }

}

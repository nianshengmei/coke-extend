package org.needcoke.coke.aop.proxy.advice;

import org.aopalliance.aop.Advice;
import java.lang.reflect.Method;

public interface CokeAdvice extends Advice {

   Method getMethod();



    void invoke(Object returnValue, Method method, Object[] args, Object target) throws Throwable ;
//    {
//        returnValue = method.invoke(target,args);
//    }
}

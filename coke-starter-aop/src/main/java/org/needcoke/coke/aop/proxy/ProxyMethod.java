package org.needcoke.coke.aop.proxy;

import lombok.Data;
import lombok.experimental.Accessors;
import org.needcoke.coke.aop.proxy.advice.*;

import java.lang.reflect.Method;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProxyMethod {

    private String beanName;

    /**
     * 源方法
     */
    private Method method;


    /**
     * 前置通知
     */
    private List<BeforeAdvice> beforeAdvices;

    /**
     * 环绕通知
     */
    private List<AroundAdvice> aroundAdvices;

    /**
     * 后置通知
     */
    private List<AfterReturningAdvice> afterReturningAdvices;

    /**
     * 异常通知
     */
    private List<ThrowsAdvice>  throwsAdvices;

    /**
     * 最终通知
     */
    private List<AfterAdvice> afterAdvices;


}

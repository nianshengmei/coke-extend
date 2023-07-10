package org.needcoke.coke.web.core;

import org.needcoke.coke.web.annotation.ControllerAdvice;
import org.needcoke.coke.web.annotation.Intercept;
import org.needcoke.coke.web.exception.ExceptionAdviceError;
import org.needcoke.coke.web.exception.ExceptionHandler;
import org.needcoke.coke.web.exception.HandlerCache;
import org.needcoke.coke.web.exception.HandlerCacheMgmt;
import org.needcoke.coke.web.interceptor.Interceptor;
import org.needcoke.coke.web.interceptor.InterceptorCacheMgmt;
import pers.warren.ioc.core.BeanDefinition;
import pers.warren.ioc.core.BeanPostProcessor;
import pers.warren.ioc.core.BeanRegister;
import pers.warren.ioc.util.ReflectUtil;

import java.lang.reflect.Method;

/**
 * http拦截器的后置处理器
 *
 * @author warren
 * @since 1.0.3
 */
public class HttpInterceptorBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(BeanDefinition beanDefinition, BeanRegister register) {
        //处理ControllerAdvice
        if (ControllerAdvice.class.isAssignableFrom(beanDefinition.getClz())) {
            Method[] declaredMethods = beanDefinition.getClz().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (ReflectUtil.containsAnnotation(declaredMethod, ExceptionHandler.class)) {
                    ExceptionHandler annotation = declaredMethod.getAnnotation(ExceptionHandler.class);
                    Class<? extends Throwable>[] catchExps = annotation.value();
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if (parameterTypes.length > 1) {
                        throw new ExceptionAdviceError("@ExceptionHandler所标注的方法只能有一个参数!且必须是注解的参数值或者超类");
                    }
                    for (Class<? extends Throwable> catchExp : catchExps) {
                        if (parameterTypes.length == 1 && !parameterTypes[0].isAssignableFrom(catchExp)) {
                            String errorMsg = String.format("@ExceptionHandler所标注的方法的参数必须只能是注解要求异常的超类或者本身。注解要求捕获的类型 %s,参数类型 %s。",
                                    catchExp.getTypeName(), parameterTypes[0].getTypeName());
                            throw new ExceptionAdviceError(errorMsg);
                        }
                        HandlerCache handlerCache = new HandlerCache();
                        handlerCache.setExceptionType(catchExp)
                                .setHandleMethod(declaredMethod)
                                .setInsertThrowable(parameterTypes.length == 1)
                                .setAdviceName(beanDefinition.getName());
                        HandlerCacheMgmt.instance.addCache(handlerCache);
                    }
                }
            }
        }

        Class<?> clz = beanDefinition.getClz();
        if (Interceptor.class.isAssignableFrom(clz) && ReflectUtil.containsAnnotation(clz, Intercept.class)) {
            Intercept interceptAnnotation = clz.getAnnotation(Intercept.class);
            String[] paths = interceptAnnotation.path();
            for (String path : paths) {
                InterceptorCacheMgmt.instance.addCache(path,beanDefinition.getName());
            }
        }
    }
}

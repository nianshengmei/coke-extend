package org.needcoke.coke.aop.util;

import cn.hutool.core.util.RandomUtil;
import lombok.experimental.UtilityClass;
import net.bytebuddy.ByteBuddy;
import org.needcoke.coke.aop.exc.ProxyException;
import org.needcoke.coke.http.ThrowsNotifyObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@UtilityClass
public class ClassUtils {

    public ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public Object createDynamicClzObject(Class<?> superClass,Object fieldObject){
        Class<?> dynamicType = new ByteBuddy()
                .subclass(superClass)
                .name(superClass.getTypeName()+".CokeProxy"+ RandomUtil.randomString(5))
                .defineField("throwsNotifyObject", ThrowsNotifyObject.class, Modifier.PUBLIC)
                .make()
                .load(superClass.getClassLoader())
                .getLoaded();
        try {
            Object o = dynamicType.newInstance();
            Field throwsNotifyObject = dynamicType.getField("throwsNotifyObject");
            throwsNotifyObject.set(o, fieldObject);
            return o;
        }catch (Throwable throwable){
            throw new ProxyException(superClass.getTypeName() + " must have a constructor with no parameter !");
        }
    }

}

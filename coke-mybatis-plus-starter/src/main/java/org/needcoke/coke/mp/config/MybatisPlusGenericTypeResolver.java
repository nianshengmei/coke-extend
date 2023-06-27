package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.core.toolkit.reflect.IGenericTypeResolver;
import pers.warren.ioc.util.GenericUtil;

public class MybatisPlusGenericTypeResolver implements IGenericTypeResolver {

    @Override
    public Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        return GenericUtil.resolveTypeArguments(clazz, genericIfc);
    }
}

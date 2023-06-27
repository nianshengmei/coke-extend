package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.warren.ioc.core.PreLoad;

public class MybatisPlusPreLoad implements PreLoad {
    @Override
    public Class<?>[] preloadBasicComponentClass() {
        return new Class[0];
    }

    @Override
    public Class<?>[] preloadBasicComponentAnnotationClass() {
        return new Class[0];
    }

    @Override
    public Class<?>[] findClasses() {
        return new Class[]{BaseMapper.class};
    }
}

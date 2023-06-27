package org.needcoke.coke.mp.config;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pers.warren.ioc.core.Container;

import java.lang.reflect.Method;


public class InvocationHandler<T> implements java.lang.reflect.InvocationHandler {

    private Class<T> mapperInterface;

    public InvocationHandler(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    private T mapper;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSession;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Container container = Container.getContainer();

        if (null != mapper) {
            Method m = mapperInterface.getMethod(method.getName(), method.getParameterTypes());
            return m.invoke(mapper, args);
        }
        if (null == sqlSessionFactory) {
            sqlSessionFactory = container.getBean(SqlSessionFactory.class);
        }
        if (null == sqlSession || sqlSession.getConnection().isClosed()) {
            sqlSession = sqlSessionFactory.openSession();
        }
        T mapper = sqlSession.getMapper(mapperInterface);
        this.mapper = mapper;
        Method m = mapperInterface.getMethod(method.getName(), method.getParameterTypes());
        return m.invoke(mapper, args);
    }
}
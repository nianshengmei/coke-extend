package org.needcoke.coke.kafka.core;

import org.needcoke.coke.aop.proxy.AdviceType;
import org.needcoke.coke.aop.proxy.MethodDeterminesProxy;
import org.needcoke.coke.aop.proxy.MethodStrategy;
import org.needcoke.coke.kafka.annotation.Producer;

public class KafkaMethodDeterminesProxy implements MethodDeterminesProxy {

    @Override
    public MethodStrategy[] methodAnnotationClasses() {
        return new MethodStrategy[]{new MethodStrategy(Producer.class, AdviceType.AFTER,"kafkaAspect","after")};
    }
}

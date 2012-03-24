package org.tanoseam.examples;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

public class MyApplicationContext implements Context
{
    ConcurrentHashMap<Contextual<?>, Object> instanceMap = new ConcurrentHashMap<Contextual<?>, Object>();

    @Override
    public Class<? extends Annotation> getScope() {
        log("MyApplicationContext::getScope");
        return MyApplicationScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        log("MyApplicationContext::get(Contextual<T>, CreationalContext<T>)");
        Object instance = instanceMap.get(contextual);
        if (instance != null) {
            log("\tGet from Context: instance=" + instance);
        }
        else {
            if (creationalContext != null) {
                instance = contextual.create(creationalContext);
            }
            log("\tPut to Context: instance=" + instance);
            instanceMap.put(contextual,instance);
        }
        //new RuntimeException("Debugging Use").printStackTrace();
        return (T) instance;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        log("MyApplicationContext::get(Contextual<T>)");
        Object instance = instanceMap.get(contextual);
        log("\tGet from Context: instance=" + instance);
        return (T) instance;
    }

    @Override
    public boolean isActive() {
        log("MyApplicationContext::isActive");
        return true;
    }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }
}

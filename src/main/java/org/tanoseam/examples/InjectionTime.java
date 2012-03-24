package org.tanoseam.examples;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.*;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class InjectionTime implements Extension
{

    public InjectionTime() {
        log("InjectionTime::constructor");
    }

     public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> pit) {
    	 

        final AnnotatedType<T> at =pit.getAnnotatedType();
        log("InjectionTime::processInjectionTarget: at=" + at);

        final InjectionTarget<T> it = pit.getInjectionTarget();
        //printInjectionTarget(it);
        
        // if (at.getBaseType().getClass().getSimpleName().equals("ManagedBeanUserManager") == false) {
        //	 return;
         //}

        InjectionTarget<T> wrapped = new InjectionTarget<T>() {

            public void inject(T instance, CreationalContext<T> ctx) {
            	  log("Begin inject: at=" + at);
                //printInjectionTarget(it);
          	      long start = System.currentTimeMillis();
                it.inject(instance, ctx);
                long end = System.currentTimeMillis();
                log("End inject: injectionTime=" + (end - start) + " ms");
            }

            public void postConstruct(T instance) {
                it.postConstruct(instance);
            }

            public void preDestroy(T instance) {
                it.dispose(instance);
            }

            public void dispose(T instance) {
                it.dispose(instance);
            }

            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            public T produce(CreationalContext<T> ctx) {
                return it.produce(ctx);
            }
        };

        pit.setInjectionTarget(wrapped);
    }

    private <T> void printInjectionTarget(InjectionTarget<T> it) {
        Set<InjectionPoint> ips = it.getInjectionPoints();

        for (InjectionPoint ip: ips) {
            log("\t\tInjectionPoint=" + ip);
            log("\t\t\tType=" + ip.getType());
            Set<Annotation> qs = ip.getQualifiers();
            for (Annotation a: qs) {
                log("\t\t\tQualifier=" + a);
            }
        }
    }
    
    void log(String messages) {
        System.out.println("CDI: " + messages);
    }
}
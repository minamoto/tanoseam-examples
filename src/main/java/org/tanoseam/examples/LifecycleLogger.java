package org.tanoseam.examples;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: minamoto
 */
public class LifecycleLogger implements Extension
{

    public LifecycleLogger() {
        System.out.println("**** MyExtensionInjectionTarget constructor");
    }

    /*
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        System.out.println("*** finished the scanning process");
        new RuntimeException("Debugging Use").printStackTrace();
    }
    */

    public <T> void processInjectionTarget(final @Observes ProcessInjectionTarget<T> pit) {

        final AnnotatedType<T> at =pit.getAnnotatedType();
        System.out.println("*** LifecycleLogger=" + at);

        final InjectionTarget<T> it = pit.getInjectionTarget();
        printInjectionTarget(it);

        InjectionTarget<T> wrapped = new InjectionTarget<T>() {
            int counter = 0;

            public void inject(T instance, CreationalContext<T> ctx) {
                it.inject(instance, ctx);
            }

            public void postConstruct(T instance) {
                it.postConstruct(instance);
                counter ++;
                System.out.println("*** postConstruct, pit=>" + pit.getAnnotatedType() + ", instance=>" + instance);
                System.out.println("*** COUNTER=" + counter);
            }

            public void preDestroy(T instance) {
                it.dispose(instance);
                System.out.println("*** preDestroy, pit=>" + pit.getAnnotatedType() + ", instance=>" + instance);
            }

            public void dispose(T instance) {
                it.dispose(instance);

            }

            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            public T produce(CreationalContext<T> ctx) {
                T result = it.produce(ctx);
                System.out.println("*** produce, ctx=>" + ctx + ", pit=>" + pit.getAnnotatedType() + ", result=>" + result);
                return result;
            }
        };

        pit.setInjectionTarget(wrapped);
    }

    private <T> void printInjectionTarget(InjectionTarget<T> it) {
        Set<InjectionPoint> ips = it.getInjectionPoints();

        for (InjectionPoint ip: ips) {
            log("\t\tInjectionPoint=" + ip);
            log("\t\t\tType=" + ip.getType());
            //Annotated atd = ip.getAnnotated();
            //log("\t\t\tAnnotated=" + atd);
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
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

/**
 * Created by IntelliJ IDEA.
 * User: minamoto
 */
public class MyExtension implements Extension
{

    public MyExtension() {
        System.out.println("**** MyExtension constructor");
    }

    /*
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        System.out.println("*** finished the scanning process");
        new RuntimeException("Debugging Use").printStackTrace();
    }
    */

    public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> pit) {

        final AnnotatedType<T> at =pit.getAnnotatedType();
        System.out.println("*** InjectionTarget=" + at);

        final InjectionTarget<T> it = pit.getInjectionTarget();
        printInjectionTarget(it);

        InjectionTarget<T> wrapped = new InjectionTarget<T>() {

            public void inject(T instance, CreationalContext<T> ctx) {
                System.out.println("*** InjectionTarget=" + at);
                System.out.println("**** inject() called");
                printInjectionTarget(it);
                //new RuntimeException("*** Debugging Use").printStackTrace();
                it.inject(instance, ctx);
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
            System.out.println("\t\tInjectionPoint=" + ip);
            System.out.println("\t\t\tType=" + ip.getType());
            //Annotated atd = ip.getAnnotated();
            //System.out.println("\t\t\tAnnotated=" + atd);
            Set<Annotation> qs = ip.getQualifiers();
            for (Annotation a: qs) {
                System.out.println("\t\t\tQualifier=" + a);
            }
        }
    }
}
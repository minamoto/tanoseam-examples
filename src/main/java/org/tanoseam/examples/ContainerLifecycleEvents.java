package org.tanoseam.examples;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessSessionBean;
import java.lang.annotation.Annotation;
import java.util.Set;

public class ContainerLifecycleEvents implements Extension {

    public ContainerLifecycleEvents() {
        log("ContainerLifecycleEvents constructor");
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event) {
        log("BeforeBeanDiscovery");
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event,
                                   BeanManager bm) {
        log("AfterBeanDiscovery");
    }

    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event,
                                          BeanManager bm) {
        log("AfterDeploymentValidation");
    }

    public void beforeShutdown(@Observes BeforeShutdown event,
                               BeanManager bm) {
        log("BeforeShutdown");
    }

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        log("ProcessAnnotatedType=" + event);
    }

    public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> event) {
        final AnnotatedType<T> at = event.getAnnotatedType();
        log("InjectionTarget=" + at);
    }

    public <T,X> void processProducer(@Observes ProcessProducer<T,X> event) {
        log("ProcessProducer=" + event.getAnnotatedMember());
    }

    public <T> void processBean(@Observes ProcessBean<T> event) {
        Bean<T> bean = event.getBean();
        log("ProcessBean=" + bean.getBeanClass() + ", scope=" + bean.getScope());
    }

    public <T> void processSessionBean(@Observes ProcessSessionBean<T> event) {
        Bean<Object> bean = event.getBean();
        log("ProcessSessionBean=" + bean.getBeanClass() + ", scope=" + bean.getScope());
    }

    public <T> void processManagedBean(@Observes ProcessManagedBean<T> event) {
        Bean<T> bean = event.getBean();
        log("ProcessManagedBean=" + bean.getBeanClass() + ", scope=" + bean.getScope());
    }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }
}

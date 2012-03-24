package org.tanoseam.examples;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class ContextRegistration implements Extension {
/*
    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event) {
        log("ContextRegistration::AfterBeanDiscovery");

        final boolean normalScope = true;
        final boolean passivating = false;
        event.addScope(MyApplicationScoped.class, normalScope, passivating);
    }
*/
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event,
                                   BeanManager bm) {
        log("ContextRegistration::AfterBeanDiscovery");

        event.addContext(new MyApplicationContext());
    }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }
}

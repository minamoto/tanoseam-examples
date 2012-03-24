package org.tanoseam.examples;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Annotation;
import java.util.Set;

public class CollectBeanInfo implements Extension {
	
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event,
                                          BeanManager bm) {
    	log("AfterDeploymentValidation");
    	/*
    	 * Caused by: org.jboss.weld.exceptions.DeploymentException: WELD-001409 Ambiguous dependencies for type [InjectionPoint] with qualifiers [@Default] at injection point [[parameter 1] of [method] 
    	 * @Produces org.jboss.weld.examples.login.Resources.getLogger(InjectionPoint)
    	 * 
    	 */
    	int count = 0;
    	Set<Bean<?>> beans = bm.getBeans(Object.class, new AnnotationLiteral<Any>(){});
    	for (Bean<?> bean: beans) {
    		log("bean=" + bean);
    		count++;
    	}
    	log("********** count=" + count);
    }

    void log(String messages) {
    	System.out.println("CDI: " + messages);
    }
}

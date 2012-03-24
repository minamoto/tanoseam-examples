package org.tanoseam.examples;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
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
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BeanRegistration implements Extension {

    public BeanRegistration() {
        log("BeanRegistration constructor");
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event,
                                   BeanManager bm) {
        log("BeanRegistration::AfterBeanDiscovery");

        AnnotatedType<MyBean> at = bm.createAnnotatedType(MyBean.class);
        final InjectionTarget<MyBean> it = bm.createInjectionTarget(at);

        Bean<MyBean> bean = new Bean<MyBean>() {
            @Override
            public Class<?> getBeanClass() {
                return MyBean.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            @Override
            public String getName() {
                return "myBean";
            }

            @Override
            public Set<Annotation> getQualifiers() {
                Set<Annotation> qualifiers = new HashSet<Annotation>();
                qualifiers.add( new AnnotationLiteral<Default>() {} );
                qualifiers.add( new AnnotationLiteral<Any>() {} );
                return qualifiers;
            }

            @Override
            public Class<? extends Annotation> getScope() {
                return SessionScoped.class;
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            @Override
            public Set<Type> getTypes() {
                Set<Type> types = new HashSet<Type>();
                types.add(MyBean.class);
                types.add(Object.class);
                return types;
            }

            @Override
            public boolean isAlternative() {
                return false;
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public MyBean create(CreationalContext<MyBean> ctx) {
                log("Bean<MyBean>::create");
                MyBean instance = it.produce(ctx);
                it.inject(instance, ctx);
                it.postConstruct(instance);
                return instance;
            }

            @Override
            public void destroy(MyBean instance,
                                CreationalContext<MyBean> ctx) {
                log("Bean<MyBean>::destroy");
                it.preDestroy(instance);
                it.dispose(instance);
                ctx.release();
            }
        };

        event.addBean(bean);
    }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }
}

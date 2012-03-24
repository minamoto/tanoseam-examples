package org.tanoseam.examples;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.tanoseam.examples.MyApplicationScoped;

@MyApplicationScoped
public class MyBean {

    @Inject
    private MyBean2 bean2;

    public MyBean() {
       log("MyBean::constructor=" + this);
    }

    @PostConstruct
    public void initialize() {
        log("MyBean::initialize");
    }

    @PreDestroy
    public void destroy() {
        log("MyBean::destroy");
    }
    /*
    public String toString() {
	return "MyBean String";
    }
    */

    public MyBean2 getBean2() { return bean2; }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }

}

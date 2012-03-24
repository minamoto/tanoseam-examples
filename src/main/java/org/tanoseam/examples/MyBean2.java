package org.tanoseam.examples;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.tanoseam.examples.MyApplicationScoped;

//@MyApplicationScoped
public class MyBean2 {

    @Inject
    private MyBean bean;

    public MyBean2() {
       log("MyBean2::constructor=" + this);
       //       new RuntimeException("Debugging Use").printStackTrace();
    }

    @PostConstruct
    public void initialize() {
        log("MyBean2::initialize");
    }

    @PreDestroy
    public void destroy() {
        log("MyBean2::destroy");
    }
    /*
    public String toString() {
	return "MyBean String";
    }
    */

    public MyBean getBean() { return bean; }

    void log(String messages) {
        System.out.println("CDI: " + messages);
    }

}

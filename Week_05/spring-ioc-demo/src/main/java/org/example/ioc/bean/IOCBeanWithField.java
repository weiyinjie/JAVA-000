package org.example.ioc.bean;

import org.example.ioc.annotation.AutoWired;
import org.example.ioc.annotation.Bean;

@Bean(name = "testautowired")
public class IOCBeanWithField implements IIOCBean {
    @AutoWired(name = "test")
    private IIOCBean iiocBean;

    @Override
    public void say(String param) {
        System.out.println("say method in IOCBeanWithField, param is: " + param);
        iiocBean.say(param);
    }
}

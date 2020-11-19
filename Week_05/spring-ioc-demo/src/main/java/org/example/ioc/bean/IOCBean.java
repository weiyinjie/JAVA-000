package org.example.ioc.bean;

import org.example.ioc.annotation.Bean;

@Bean(name = "test")
public class IOCBean implements IIOCBean {

    @Override
    public void say(String param) {
        System.out.println("say method in IOCBean, param is: " + param);
    }
}

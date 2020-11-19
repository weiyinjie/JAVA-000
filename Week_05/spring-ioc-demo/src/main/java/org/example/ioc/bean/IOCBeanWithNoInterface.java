package org.example.ioc.bean;

import org.example.ioc.annotation.Bean;

@Bean(name = "test1")
public class IOCBeanWithNoInterface {
    public void say(String param) {
        System.out.println("mothod say in IOCBeanWithNoInterface");
    }
}

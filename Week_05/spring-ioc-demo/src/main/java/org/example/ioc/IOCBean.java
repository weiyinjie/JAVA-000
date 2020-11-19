package org.example.ioc;

@Bean(name = "test")
public class IOCBean {

    public void say() {
        System.out.println("simple bean");
    }
}

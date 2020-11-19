package org.example.ioc;

@Bean(name = "test")
public class IOCBean {

    @Override
    public String toString() {
        return "bean";
    }
}

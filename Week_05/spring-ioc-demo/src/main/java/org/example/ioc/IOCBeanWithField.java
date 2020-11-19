package org.example.ioc;

@Bean(name = "testautowired")
public class IOCBeanWithField {
    @AutoWired(name = "test")
    private IOCBean iocBean;

    public void say() {
        System.out.println("method in IOCBeanWithField");
        iocBean.say();
    }
}

package org.example.ioc;

public class TestIOC {

    public static void main(String[] args) {
        IOCBean iocBean = (IOCBean) BeanFactory.getBean("test");
        iocBean.say();

        IOCBeanWithField iocBeanWithField = (IOCBeanWithField)BeanFactory.getBean("testautowired");
        iocBeanWithField.say();
    }
}

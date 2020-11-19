package org.example.ioc;

public class TestIOC {

    public static void main(String[] args) {
        IIOCBean iiocBean = (IIOCBean) BeanFactory.getBean("test");
        iiocBean.say("param1");

//        IIOCBean iocBeanWithField = (IIOCBean) BeanFactory.getBean("testautowired");
//        iocBeanWithField.say("param1");
    }
}

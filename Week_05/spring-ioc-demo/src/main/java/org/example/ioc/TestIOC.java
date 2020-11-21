package org.example.ioc;

import org.example.ioc.bean.IIOCBean;
import org.example.ioc.bean.IOCBeanLoadByXML;
import org.example.ioc.bean.IOCBeanWithNoInterface;

public class TestIOC {

    public static void main(String[] args) {
//        IIOCBean iiocBean = (IIOCBean) BeanFactory.getBean("test");
//        iiocBean.say("param1");

        IOCBeanLoadByXML iocBeanLoadByXML = (IOCBeanLoadByXML) BeanFactory.getBean("beanLoadByXML");
        iocBeanLoadByXML.say();
//        IIOCBean iocBeanWithField = (IIOCBean) BeanFactory.getBean("testautowired");
//        iocBeanWithField.say("param1");
    }
}

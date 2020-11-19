package org.example.ioc;

import org.example.ioc.bean.IIOCBean;
import org.example.ioc.bean.IOCBeanWithNoInterface;

public class TestIOC {

    public static void main(String[] args) {
//        IIOCBean iiocBean = (IIOCBean) BeanFactory.getBean("test");
//        iiocBean.say("param1");

        IOCBeanWithNoInterface iocBeanWithNoInterface = (IOCBeanWithNoInterface) BeanFactory.getBean("test1");
        iocBeanWithNoInterface.say("param2");
//        IIOCBean iocBeanWithField = (IIOCBean) BeanFactory.getBean("testautowired");
//        iocBeanWithField.say("param1");
    }
}

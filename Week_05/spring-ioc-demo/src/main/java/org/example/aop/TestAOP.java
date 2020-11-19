package org.example.aop;

import org.example.aop.annotation.After;
import org.example.aop.annotation.Before;
import org.example.aop.annotation.PointCut;
import org.example.ioc.Bean;

@Bean
@PointCut(beanName = "test", methodName = "say")
public class TestAOP {

    @Before
    public void before(String param) {
        System.out.println("before method invoke, param is: " + param);
    }

    @After
    public void customAfter(String param) {
        System.out.println("after method invoke, param is: " + param);
    }
}

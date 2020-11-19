package org.example.aop.annotation;

import java.lang.annotation.*;

/**
 * 定义切点
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PointCut {
    /**
     * 需要AOP增强的bean名称
     *
     * @return
     */
    String beanName() default "";

    /**
     * 需要AOP增强的方法名称
     *
     * @return
     */
    String methodName() default "";
}

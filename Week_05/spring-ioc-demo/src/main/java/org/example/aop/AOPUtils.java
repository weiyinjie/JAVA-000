package org.example.aop;

import net.sf.cglib.proxy.Enhancer;
import org.example.aop.annotation.After;
import org.example.aop.annotation.Before;
import org.example.aop.handler.AOPCglibProxy;
import org.example.aop.handler.AOPHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class AOPUtils {

    /**
     * @param pointCutObj 切点对象
     * @param obj         需要被代理的对象
     * @return
     */
    public static Object getProxyObj(Object pointCutObj, Object obj, String methodName) {
        // @PointCut类 解析出前置&后置方法
        Method before = null;
        Method after = null;
        for (Method declaredMethod : pointCutObj.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Before.class)) {
                before = declaredMethod;
            } else if (declaredMethod.isAnnotationPresent(After.class)) {
                after = declaredMethod;
            }
        }
        if (before == null && after == null) {
            return obj;
        }
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            // jdk代理
            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), interfaces, new AOPHandler(before, after, pointCutObj, obj, methodName));
        } else {
            // cglib代理
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(obj.getClass());
            enhancer.setCallback(new AOPCglibProxy(before, after, pointCutObj, obj, methodName));
            return enhancer.create();
        }
    }
}

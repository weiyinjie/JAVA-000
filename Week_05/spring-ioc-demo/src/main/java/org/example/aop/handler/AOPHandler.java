package org.example.aop.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AOPHandler implements InvocationHandler {

    /**
     * 前置方法
     */
    private final Method before;

    /**
     * 后置方法
     */
    private final Method after;

    /**
     * 切点对象
     */
    private final Object pointCutObj;

    /**
     * 实际对象
     */
    private final Object obj;

    /**
     * 需要增强的方法名
     */
    private final String methodName;

    public AOPHandler(Method before, Method after, Object pointCutObj, Object obj, String methodName) {
        this.before = before;
        this.after = after;
        this.pointCutObj = pointCutObj;
        this.obj = obj;
        this.methodName = methodName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(methodName)) {
            if (before != null) {
                before.invoke(pointCutObj, args);
            }
            Object result = method.invoke(obj, args);
            if (after != null) {
                after.invoke(pointCutObj, args);
            }
            return result;
        }
        return null;
    }
}

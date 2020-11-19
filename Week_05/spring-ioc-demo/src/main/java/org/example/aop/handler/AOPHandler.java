package org.example.aop.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AOPHandler implements InvocationHandler {

    private final Method before;

    private final Method after;

    private final Object pointCutObj;

    private final Object obj;

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
            method.invoke(obj, args);
            if (after != null) {
                after.invoke(pointCutObj, args);
            }
        }
        return null;
    }
}

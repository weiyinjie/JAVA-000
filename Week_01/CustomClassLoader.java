package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) {
        File file = new File("/Users/weiyinjie/Downloads/Hello/Hello.xlass");
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object hello = new CustomClassLoader().findClass("Hello").newInstance();
        hello.getClass().getDeclaredMethod("hello").invoke(hello);
    }
}

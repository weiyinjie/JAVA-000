package org.example.ioc;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.aop.AOPUtils;
import org.example.aop.annotation.PointCut;
import org.example.ioc.annotation.AutoWired;
import org.example.ioc.annotation.Bean;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactory {

    /**
     * bean容器
     */
    private static final Map<String, Object> BEANS = new HashMap<>();

    /**
     * 所有类名
     */
    private static final List<String> CLAZZ_NAMES = new ArrayList<>();

    static {
        URL url = BeanFactory.class.getClassLoader().getResource("");
        String path = url.getPath();
        String packName = "";
        // 加载所有类名
        loadClassNames(packName, new File(path));
        // 加载所有bean
        loadClasses();
        // 加载AOP增强
        loadAOP();
        // 加载依赖类
        loadAutowired();
    }


    /**
     * 加载AOP
     */
    private static void loadAOP() {
        if (!BEANS.isEmpty()) {
            BEANS.forEach((k, v) -> {
                Class<?> aClass = v.getClass();
                if (aClass.isAnnotationPresent(PointCut.class)) {
                    PointCut annotation = aClass.getAnnotation(PointCut.class);
                    // 需要增强的bean名称以及其方法名
                    String beanName = annotation.beanName();
                    try {
                        BEANS.put(beanName, AOPUtils.getProxyObj(aClass.newInstance(), BEANS.get(beanName), annotation.methodName()));
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    private static void loadAutowired() {
        if (!BEANS.isEmpty()) {
            BEANS.forEach((k, v) -> {
                for (Field declaredField : v.getClass().getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(AutoWired.class)) {
                        declaredField.setAccessible(true);
                        AutoWired annotation = declaredField.getAnnotation(AutoWired.class);
                        String beanName = annotation.name().equals("") ? declaredField.getClass().getSimpleName().toLowerCase() : annotation.name();
                        try {
                            declaredField.set(v, BEANS.get(beanName));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private static void loadClasses() {
        if (CLAZZ_NAMES.size() > 0) {
            for (String clazzName : CLAZZ_NAMES) {
                try {
                    Class<?> aClass = Class.forName(clazzName);
                    if (aClass.isAnnotationPresent(Bean.class)) {
                        Bean annotation = aClass.getAnnotation(Bean.class);
                        String beanName = annotation.name().equals("") ? clazzName.substring(clazzName.lastIndexOf('.') + 1) : annotation.name();
                        BEANS.put(beanName.toLowerCase(), aClass.newInstance());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        loadClassesByXML();
    }

    /**
     * 解析xml加载类
     */
    private static void loadClassesByXML() {
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document read = saxReader.read(resourceAsStream);
            Element rootElement = read.getRootElement();
            List<Element> list = rootElement.selectNodes("//bean");
            for (int i = 0; i < list.size(); i++) {
                Element element = list.get(i);
                String name = element.attributeValue("name");
                String className = element.attributeValue("class");
                BEANS.put(name.toLowerCase(), Class.forName(className).newInstance());
            }
        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void loadClassNames(String packName, File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                loadClassNames(packName + (packName.equals("") ? "" : ".") + listFile.getName(), listFile);
            }
        } else {
            if (packName.endsWith(".class")) {
                CLAZZ_NAMES.add(packName.replace(".class", ""));
            }
        }
    }


    public static Object getBean(String name) {
        return BEANS.get(name.toLowerCase());
    }
}

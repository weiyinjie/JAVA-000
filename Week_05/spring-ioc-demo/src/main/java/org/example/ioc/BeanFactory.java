package org.example.ioc;

import java.io.File;
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
        CLAZZ_NAMES.forEach(System.out::println);
        // 加载所有bean
        loadClasses();
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
        System.out.println(BEANS);
    }

    private static void loadClassNames(String packName, File file) {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                loadClassNames(packName + (packName.equals("") ? "" : ".") + listFile.getName(), listFile);
            }
        } else {
            CLAZZ_NAMES.add(packName.replace(".class", ""));
        }
    }

    public static Object getBean(String name) {
        return BEANS.get(name.toLowerCase());
    }
}

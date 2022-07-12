package top.mnsx.my_spring.container;

import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.Qualifier;
import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.annotation.bean.Controller;
import top.mnsx.my_spring.annotation.bean.Service;
import top.mnsx.my_spring.exception.*;
import top.mnsx.my_spring.generator.AnnotationListGenerator;
import top.mnsx.my_spring.parser.XmlSpringConfigParser;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.container
 * @CreateTime: 2022/7/10
 * @Dscription: 容器类
 */

public class ClassPathXmlApplicationContext {

    private String springConfig;

    //扫描基本路径后得到的所有类集合
    private List<String> classPaths = new ArrayList<>();

    //Spring中IOC名字容器
    private Map<String, List<Object>> iocNameContainer = new ConcurrentHashMap<>();

    //Spring中IOC类容器
    private Map<Class<?>, Object> iocClassContainer = new ConcurrentHashMap<>();

    //Spring中接口容器
    private Map<Class<?>, List<Object>> iocInterfacesContainer = new ConcurrentHashMap<>();

    public ClassPathXmlApplicationContext(String springConfig) {
        this.springConfig = springConfig;
        refresh();
    }

    /**
     * 根据接口查找类
     * @param c
     * @return
     */
    public Object getBeans(Class<?> c) {
        List<Object> objects = iocInterfacesContainer.get(c);
        if (objects.size() == 1) {
            return objects.get(0);
        } else {
            throw new ToMuchBeanException("存在多个bean匹配接口");
        }
    }

    /**
     * 根据类查找bean
     * @param aClass
     * @return
     */
    public Object getBean(Class<?> aClass) {
        return iocClassContainer.get(aClass);
    }

    /**
     * 根据名字查找bean
     * @param name
     * @return
     */
    public List<Object> getBeans(String name) {
        if (iocNameContainer.containsKey(name)) {
            return iocNameContainer.get(name);
        } else {
            throw new NotFoundBeanMatchConditionException("没有找到满足适配条件的bean");
        }
    }

    /**
     * 初始化方法
     */
    private void refresh() {
        String basePackage = XmlSpringConfigParser.getBasePackage(springConfig);
        //加载所有类
        this.loadClasses(basePackage);
        //执行类的初始化
        this.doInitInstance();
        //实现对象的依赖注入
        this.doInjection();
    }

    /**
     * 实现对象的依赖注入
     */
    private void doInjection() {
        Set<Class<?>> classes = iocClassContainer.keySet();
        if (!classes.isEmpty()) {
            for (Class<?> aClass : classes) {
                Field[] declaredFields = aClass.getDeclaredFields();
                if (declaredFields.length != 0) {
                    for (Field field : declaredFields) {
                        Object o = null;
                        boolean flag = false;
                        boolean annoAutowired = field.isAnnotationPresent(Autowired.class);
                        boolean annoQualifier = field.isAnnotationPresent(Qualifier.class);
                        if (annoQualifier) {
                            Qualifier qualifier = field.getAnnotation(Qualifier.class);
                            String value = qualifier.value();
                            if (!"".equals(value)) {
                                List<Object> beans = this.getBeans(value);
                                if (beans.size() == 1) {
                                    o = beans.get(0);
                                    flag = true;
                                }
                            }
                        }
                        if (!flag) {
                            o = this.findBeanByType(field, annoAutowired);
                        }
                        //对象匹配到之后，通过反射，注入给属性
                        field.setAccessible(true);
                        //反射属性赋值
                        try {
                            field.set(iocClassContainer.get(aClass), o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 通过类查找bean
     * @param field
     * @param annoAutowired
     * @return
     */
    private Object findBeanByType(Field field, boolean annoAutowired) {
        if (annoAutowired) {
            return this.getBean(field.getType()) != null ? this.getBean(field.getType()) : this.getBeans(field.getType());
        } else {
            throw new NotFoundBeanMatchConditionException("没有找到满足适配条件的bean");
        }
    }

    /**
     * 把IOC容器中类进行实例化
     */
    private void doInitInstance() {
        for (String classPath : classPaths) {
            try {
                Class<?> c = Class.forName(classPath);
                List<Class<? extends Annotation>> annotationList = AnnotationListGenerator.getAnnotationList();

                for (Class<? extends Annotation> annotation : annotationList) {
                    if (c.isAnnotationPresent(annotation)) {
                        Object o = c.newInstance();

                        Class<?>[] interfaces = c.getInterfaces();
                        if (interfaces.length != 0) {
                            for (Class<?> aInterface : interfaces) {
                                List<Object> objects = iocInterfacesContainer.get(aInterface);
                                if (objects == null) {
                                    List<Object> objs = new ArrayList<>();
                                    objs.add(o);
                                    iocInterfacesContainer.put(aInterface, objs);
                                } else {
                                    objects.add(o);
                                }
                            }
                        }

                        iocClassContainer.put(c, o);

                        Controller controllerAnnotation = c.getAnnotation(Controller.class);
                        Service serviceAnnotation = c.getAnnotation(Service.class);
                        Component componentAnnotation = c.getAnnotation(Component.class);

                        String value = "";
                        if (controllerAnnotation != null) {
                            value = controllerAnnotation.value();
                        } else if (serviceAnnotation != null) {
                            value = serviceAnnotation.value();
                        } else if (componentAnnotation != null) {
                            value = componentAnnotation.value();
                        }
                        String objName = "";
                        if ("".equals(value)) {
                            String className = c.getSimpleName();

                            objName = String.valueOf(className.charAt(0)).toLowerCase() + className.substring(1);
                        } else {
                            objName = value;
                        }
                        if (iocNameContainer.containsKey(objName)) {
                            iocNameContainer.get(objName).add(o);
                        } else {
                            List<Object> list = new ArrayList<>();
                            list.add(o);
                            iocNameContainer.put(objName, list);
                        }
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    /**
     * 加载基础包下的所有class
     */
    private void loadClasses(String basePackage) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");

        basePackage = basePackage.replace(".", File.separator);

        File file = null;
        if (url != null) {
            file = new File(url.toString().replace("file:/", "") + basePackage);
        } else {
            throw new UrlNotFoundException("无法找到指定的url");
        }

        String path = file.toString();
        if (path.contains("test-classes")) {
            path = path.replace("test-classes", "classes");
        }

        this.findAllClasses(new File(path));
    }

    /**
     * 遍历basePackage中所以的class文件
     * @param path
     */
    private void findAllClasses(File path) {
        File[] files = path.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                String filePath = file.getPath();
                if (filePath.endsWith("class")) {
                    filePath = this.handlerPath(filePath);
                    classPaths.add(filePath);
                }
            } else {
                this.findAllClasses(file);
            }
        }
    }

    /**
     * 解析文件路径
     * @param path
     * @return
     */
    private String handlerPath(String path) {
        int index = path.indexOf("classes\\");
        path = path.substring(index + 8, path.length() - 6);
        path = path.replace(File.separator, ".");
        return path;
    }
}

package top.mnsx.my_spring.container;

import top.mnsx.my_spring.annotation.*;
import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.annotation.bean.Controller;
import top.mnsx.my_spring.annotation.bean.Repository;
import top.mnsx.my_spring.annotation.bean.Service;
import top.mnsx.my_spring.exception.*;
import top.mnsx.my_spring.generator.AnnotationListGenerator;
import top.mnsx.my_spring.jdbc.TransactionManager;
import top.mnsx.my_spring.parser.XmlSpringConfigParser;
import top.mnsx.my_spring.proxy.JdkProxy;
import top.mnsx.my_spring.transaction.TransactionProxy;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

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

    //存储切面类的集合
    private Set<Class<?>> aopClasses = new CopyOnWriteArraySet<>();

    //要被动态代理的类
    private Set<Class<?>> proxyClassSet = new CopyOnWriteArraySet<>();

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
        return objects.get(0);
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
        this.loadClasses("top.mnsx.my_spring");
        //执行类的初始化
        this.doInitInstance();
        //实现AOP，创建代理对象
        this.doAop();
        //实现对象的依赖注入
        this.doInjection();
    }

    /**
     * 执行Aop操作，创建代理对象
     */
    private void doAop() {
        if (aopClasses.size() > 0) {
            try {
                for (Class<?> aopClass : aopClasses) {
                    Method[] declaredMethods = aopClass.getDeclaredMethods();
                    if (declaredMethods.length > 0) {
                        for (Method method : declaredMethods) {
                            boolean annotationPresent = method.isAnnotationPresent(Around.class);
                            if (annotationPresent) {
                                Around around = method.getAnnotation(Around.class);
                                //获取切入点表达式
                                String execution = around.execution();

                                String fullClass = execution.substring(0, execution.lastIndexOf("."));
                                String proxyMethod = execution.substring(execution.lastIndexOf(".") + 1);

                                Class<?> targetClass = Class.forName(fullClass);
                                proxyClassSet.add(targetClass);

                                Object targetObject = iocClassContainer.get(targetClass);

                                //生成代理类之前先注入依赖
                                doInjectionByOneClass(targetClass);

                                //生成代理类
                                JdkProxy jdkProxy = new JdkProxy(targetClass, aopClass, targetObject, proxyMethod, method);
                                Object proxyInstance = jdkProxy.getProxyInstance();

                                //替换由类查找的容器
                                iocClassContainer.put(targetClass, proxyInstance);

                                //替换由名字查找的容器
                                String simpleName = targetClass.getSimpleName();
                                String className = String.valueOf(simpleName.charAt(0)).toUpperCase() + simpleName.substring(1);
                                Service service = targetClass.getAnnotation(Service.class);
                                Controller controller = targetClass.getAnnotation(Controller.class);
                                Component component = targetClass.getAnnotation(Component.class);
                                Repository repository = targetClass.getAnnotation(Repository.class);

                                String value = "";
                                if (controller != null) {
                                    value = controller.value();
                                } else if (service != null) {
                                    value = service.value();
                                } else if (component != null) {
                                    value = component.value();
                                } else if (repository != null) {
                                    value = repository.value();
                                }

                                if (!"".equals(value)) {
                                    className = value;
                                }

                                List<Object> objects = iocNameContainer.get(className);
                                if (objects != null) {
                                    for (int i = 0; i < objects.size(); ++i) {
                                        Object o = objects.get(i);
                                        if (o.getClass() == targetClass) {
                                            objects.set(i, proxyInstance);
                                        }
                                    }
                                }
                                iocNameContainer.put(className, objects);

                                //替换接口容器中数据
                                Class<?>[] interfaces = targetClass.getInterfaces();
                                if (interfaces != null) {
                                    for (Class<?> anInterface : interfaces) {
                                        List<Object> obj = iocInterfacesContainer.get(anInterface);
                                        if (obj != null) {
                                            for (int i = 0; i < obj.size(); ++i) {
                                                Object o = obj.get(i);
                                                if (o.getClass() == targetClass) {
                                                    obj.set(i, proxyInstance);
                                                    break;
                                                }
                                            }
                                        }
                                        iocInterfacesContainer.put(anInterface, obj);
                                    }
                                }
                            }

                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 实现对象的依赖注入
     */
    private void doInjection() {
        Set<Class<?>> classes = iocClassContainer.keySet();
        if (!classes.isEmpty()) {
            for (Class<?> aClass : classes) {
                if (!proxyClassSet.contains(aClass)) {
                    doInjectionByOneClass(aClass);
                }
            }
        }
    }

    private void doInjectionByOneClass(Class<?> aClass) {
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
                        } else {
                            o = this.findBeanByType(field, annoAutowired);
                            if (o == null) {
                                throw new NotFoundBeanMatchConditionException("没有找到满足适配条件的bean");
                            }
                        }
                    }
                } else if (annoAutowired) {
                    o = this.findBeanByType(field, true);
                    if (o == null) {
                        throw new NotFoundBeanMatchConditionException("没有找到满足适配条件的bean");
                    }
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

    /**
     * 通过类查找bean
     * @param field
     * @param annoAutowired
     * @return
     */
    private Object findBeanByType(Field field, boolean annoAutowired) {
        if (annoAutowired) {
            return this.getBean(field.getType()) != null ? this.getBean(field.getType()) : this.getBeans(field.getType());
        }
        return null;
    }

    /**
     * 把IOC容器中类进行实例化
     */
    private void doInitInstance() {
        for (String classPath : classPaths) {
            try {
                Class<?> c = Class.forName(classPath);

                //判断那些类需要进行AOP操作
                if (c.isAnnotationPresent(Aspect.class)) {
                    aopClasses.add(c);
                    continue;
                }

                //判断是否需要被加入IOC容器
                List<Class<? extends Annotation>> annotationList = AnnotationListGenerator.getAnnotationList();

                for (Class<? extends Annotation> annotation : annotationList) {
                    if (c.isAnnotationPresent(annotation)) {
                        Object o = c.newInstance();

                        //判断这个类是否需要进行事务管理
                        Method[] declaredMethods = c.getDeclaredMethods();
                        List<String> transactionalMethods = new ArrayList<>();
                        if (declaredMethods.length != 0) {
                            for (Method method : declaredMethods) {
                                boolean annotationPresent = method.isAnnotationPresent(Transactional.class);
                                if (annotationPresent) {
                                    transactionalMethods.add(method.getName());
                                }
                            }
                        }
                        //如果类的方法声明了Transactional注解，那么提供事务管理
                        if (transactionalMethods.size() > 0) {
                            TransactionProxy transactionProxy = new TransactionProxy(c, transactionalMethods);
                            o = transactionProxy.getProxyInstance();
                        }

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
                        Repository repositoryAnnotation = c.getAnnotation(Repository.class);

                        String value = "";
                        if (controllerAnnotation != null) {
                            value = controllerAnnotation.value();
                        } else if (serviceAnnotation != null) {
                            value = serviceAnnotation.value();
                        } else if (componentAnnotation != null) {
                            value = componentAnnotation.value();
                        } else if (repositoryAnnotation != null) {
                            value = repositoryAnnotation.value();
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

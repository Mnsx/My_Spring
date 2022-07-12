package top.mnsx.my_spring.container;

import top.mnsx.my_spring.exception.UrlNotFoundException;
import top.mnsx.my_spring.generator.AnnotationListGenerator;
import top.mnsx.my_spring.parser.XmlSpringConfigParser;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public ClassPathXmlApplicationContext(String springConfig) {
        this.springConfig = springConfig;
        refresh();
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
    }

    /**
     * 把IOC容器中类进行实例化
     */
    private void doInitInstance() {
        for (String classPath : classPaths) {
            try {
                Class<?> c = Class.forName(classPath);
                List<Class<? extends Annotation>> annotationList = AnnotationListGenerator.getAnnotationList();

                boolean flag = false;
                for (Class<? extends Annotation> annotation : annotationList) {
                    if (c.isAnnotationPresent(annotation)) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载基础包下的所有class
     */
    private void loadClasses(String basePackage) {
        URL url = ClassPathXmlApplicationContext.class.getClassLoader().getResource("");

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

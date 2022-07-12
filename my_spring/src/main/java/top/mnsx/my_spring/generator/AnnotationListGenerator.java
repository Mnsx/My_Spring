package top.mnsx.my_spring.generator;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.generator
 * @CreateTime: 2022/7/10
 * @Description: 获取annotation包下所有的类的集合
 */
public class AnnotationListGenerator {
    public static List<Class<? extends Annotation>> getAnnotationList() {
       List<Class<? extends Annotation>> annotationList = new ArrayList<>();

        try {
            Enumeration<URL> urls = AnnotationListGenerator.class.getClassLoader().getResources("top/mnsx/my_spring/annotation/bean");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File file = new File(url.getPath());
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getPath();
                    int index = fileName.indexOf("classes\\");
                    fileName = fileName.substring(index + 8, fileName.length() - 6);
                    fileName = fileName.replace(File.separator, ".");
                    Class<?> c  = Class.forName(fileName);
                    annotationList.add((Class<? extends Annotation>) c);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return annotationList;
    }
}

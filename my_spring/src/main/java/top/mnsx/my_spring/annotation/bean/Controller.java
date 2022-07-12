package top.mnsx.my_spring.annotation.bean;

import top.mnsx.my_spring.annotation.MyAnno;

import java.lang.annotation.*;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.container
 * @CreateTime: 2022/7/10
 * @Dscription: 容器类
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Controller {
    String value() default "";
}

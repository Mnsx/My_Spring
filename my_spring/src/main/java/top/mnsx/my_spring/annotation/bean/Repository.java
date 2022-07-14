package top.mnsx.my_spring.annotation.bean;

import java.lang.annotation.*;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.annotation.bean
 * @CreateTime: 2022/7/14
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Repository {
    String value() default "";
}

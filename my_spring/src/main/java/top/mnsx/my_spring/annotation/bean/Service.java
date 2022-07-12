package top.mnsx.my_spring.annotation.bean;

import java.lang.annotation.*;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.container
 * @CreateTime: 2022/7/10
 * @Dscription: Service注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Service {
    String value() default "";
}

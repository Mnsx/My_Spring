package top.mnsx.template.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.template.entity
 * @CreateTime: 2022/7/14
 * @Description: 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
   private int id;
   private String name;
   private int balance;
}

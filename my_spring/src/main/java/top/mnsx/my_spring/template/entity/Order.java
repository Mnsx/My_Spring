package top.mnsx.my_spring.template.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int oid;
    private String title;
    private String creatTime;
    private double money;
}

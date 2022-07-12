package top.mnsx.my_spring;

import top.mnsx.my_spring.container.ClassPathXmlApplicationContext;
import top.mnsx.my_spring.template.service.OrderService;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring
 * @CreateTime: 2022/7/10
 * @Description: 测试类
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(context.getBeans(OrderService.class));
    }
}

package top.mnsx.my_spring;

import top.mnsx.my_spring.container.ClassPathXmlApplicationContext;
import top.mnsx.template.controller.UserController;
import top.mnsx.template.service.UserService;
import top.mnsx.template.service.impl.UserServiceImpl;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring
 * @CreateTime: 2022/7/10
 * @Description: 启动类
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserController userController = (UserController) context.getBean(UserController.class);
        userController.transferMoney(1, 2, 100);
    }
}

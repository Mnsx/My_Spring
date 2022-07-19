package top.mnsx.template.controller;

import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.bean.Controller;
import top.mnsx.template.service.UserService;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.template.controller
 * @CreateTime: 2022/7/18
 * @Description:
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public void transferMoney(Integer fromId, Integer toId, Integer money) {
        Integer res = userService.transfer(fromId, toId, money);
        System.out.println(res > 0 ? "转账成功" : "转账失败");
    }
}

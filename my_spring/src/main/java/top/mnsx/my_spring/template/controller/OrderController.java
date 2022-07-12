package top.mnsx.my_spring.template.controller;

import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.Qualifier;
import top.mnsx.my_spring.annotation.bean.Controller;
import top.mnsx.my_spring.template.service.OrderService;

@Controller
public class OrderController {

    @Autowired
    @Qualifier("test")
    private OrderService orderService;

    public void showOrders() {
        orderService.findOrders();
    }
}

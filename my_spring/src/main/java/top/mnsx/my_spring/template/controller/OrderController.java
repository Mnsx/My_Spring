package top.mnsx.my_spring.template.controller;

import org.springframework.stereotype.Controller;
import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.template.service.OrderService;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    public void showOrders() {
        orderService.findOrders();
    }
}

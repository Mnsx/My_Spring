package top.mnsx.my_spring.template.service.impl;

import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.annotation.bean.Service;
import top.mnsx.my_spring.template.service.OrderService;
import top.mnsx.my_spring.template.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component("test")
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> findOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(111, "电子产品", "2020-10-10", 3000));
        orders.add(new Order(112, "图书产品", "2020-10-10", 3000));
        orders.add(new Order(113, "运动产品", "2020-10-10", 8888));
        Logger.getGlobal().log(Level.INFO, "order添加成功");
        return orders;
    }
}

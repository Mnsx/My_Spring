package top.mnsx.my_spring.template.service.impl;

import org.springframework.stereotype.Service;
import top.mnsx.my_spring.template.service.OrderService;
import top.mnsx.my_spring.template.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> findOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(111, "电子产品", "2020-10-10", 3000));
        orders.add(new Order(112, "图书产品", "2020-10-10", 3000));
        orders.add(new Order(113, "运动产品", "2020-10-10", 8888));
        return orders;
    }
}

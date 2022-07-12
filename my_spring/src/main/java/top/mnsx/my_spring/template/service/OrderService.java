package top.mnsx.my_spring.template.service;

import top.mnsx.my_spring.annotation.bean.Service;
import top.mnsx.my_spring.template.entity.Order;

import java.util.List;

@Service
public interface OrderService {
    List<Order> findOrders();
}

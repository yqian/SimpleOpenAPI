package org.yqian.online.orders.controller;

import org.yqian.online.orders.codegen.api.OrdersApi;
import org.yqian.online.orders.codegen.model.Order;
import org.yqian.online.orders.exception.OrderNotFoundException;
import org.yqian.online.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrderController implements OrdersApi {

    @Autowired
    OrderService orderService;

    @Override
    public ResponseEntity<Order> listOrderById(Integer orderId) {
        Order order;
        try {
            order = orderService.getOrderById(orderId);
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order Not Found", e);
        }
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity placeOrder(Order order) {
       return new ResponseEntity(orderService.placeOrder(order), HttpStatus.CREATED);
    }
}

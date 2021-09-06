package edu.yqian.online.orders.service;

import edu.yqian.online.orders.codegen.model.Order;
import edu.yqian.online.orders.entity.OrderEntity;
import edu.yqian.online.orders.entity.ProductEntity;
import edu.yqian.online.orders.exception.OrderNotFoundException;
import edu.yqian.online.orders.repository.OrderRepository;
import edu.yqian.online.orders.transform.OrderEntityTransformer;
import edu.yqian.online.orders.transform.ProductEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderEntityTransformer transformer;

    @Autowired
    private OrderRepository orderRepository;

    public Order normalOrder(Order order) {
        log.info(order.toString());
        OrderEntity orderEntity = transformer.transformToEntity(order);
        orderEntity.setTotal(orderEntity.getProductList().stream().mapToDouble(p -> p.getQuantity() * p.getPrice()).sum());
        OrderEntity result = orderRepository.save(orderEntity);
        return transformer.transformFromEntity(result);
    }

    public Order offerOrder(Order order) {
        log.info("Customer gets discount of Apple BOGO and Orange B2GO");
        OrderEntity orderEntity = transformer.transformToEntity(order);
        double total = 0;
        int quantityOfApple = orderEntity.getProductList().stream().filter(p -> p.getName().equalsIgnoreCase(ProductEnum.APPLE.getName())).map(ProductEntity::getQuantity).findAny().orElse(0);
        if (quantityOfApple != 0) {
            int dealQuantity = quantityOfApple - quantityOfApple / 2;
            total = dealQuantity * ProductEnum.APPLE.getPrice();
        }

        int quantityOfOrange = orderEntity.getProductList().stream().filter(p -> p.getName().equalsIgnoreCase(ProductEnum.ORANGE.getName())).map(ProductEntity::getQuantity).findAny().orElse(0);

        if (quantityOfOrange != 0) {
            int dealQuantity = quantityOfOrange - quantityOfOrange / 3;
            total += dealQuantity * ProductEnum.ORANGE.getPrice();
        }
        orderEntity.setTotal(total);
        orderRepository.save(orderEntity);
        return transformer.transformFromEntity(orderEntity);
    }

    public Order getOrderById(int orderId) throws OrderNotFoundException {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return transformer.transformFromEntity(order);
    }
}

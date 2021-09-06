package edu.yqian.online.orders.transform;

import edu.yqian.online.orders.codegen.model.Order;
import edu.yqian.online.orders.codegen.model.Product;
import edu.yqian.online.orders.entity.OrderEntity;
import edu.yqian.online.orders.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderEntityTransformer {

    public OrderEntity transformToEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productApple = order.getProducts().stream().filter(p -> p.getName().equalsIgnoreCase(ProductEnum.APPLE.getName())).findAny().map(p -> ProductEntity.builder().name(p.getName()).quantity(p.getQuantity()).build()).orElse(null);
        if (productApple != null) {
            productApple.setPrice(ProductEnum.APPLE.getPrice());
            productEntities.add(productApple);
        }
        ProductEntity productOrange = order.getProducts().stream().filter(p -> p.getName().equalsIgnoreCase(ProductEnum.ORANGE.getName())).findAny().map(p -> ProductEntity.builder().name(p.getName()).quantity(p.getQuantity()).build()).orElse(null);
        if (productOrange != null) {
            productOrange.setPrice(ProductEnum.ORANGE.getPrice());
            productEntities.add(productOrange);
        }
        orderEntity.setProductList(productEntities);
        return orderEntity;
    }

    public Order transformFromEntity(OrderEntity orderEntity) {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        for (ProductEntity p : orderEntity.getProductList()) {
            Product product = new Product();
            product.setProductId(Integer.toString(p.getProductId()));
            product.setName(p.getName());
            product.setQuantity(p.getQuantity());
            product.setPrice(BigDecimal.valueOf(p.getPrice()));
            products.add(product);
        }
        order.setProducts(products);
        order.setOrderId(Integer.toString(orderEntity.getOrderId()));
        order.setTotal(BigDecimal.valueOf(orderEntity.getTotal()));
        return order;
    }
}

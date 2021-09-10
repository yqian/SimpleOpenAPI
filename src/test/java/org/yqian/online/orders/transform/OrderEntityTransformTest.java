package org.yqian.online.orders.transform;

import org.yqian.online.orders.codegen.model.Order;
import org.yqian.online.orders.codegen.model.Product;
import org.yqian.online.orders.entity.OrderEntity;
import org.yqian.online.orders.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrderEntityTransformTest {

    @InjectMocks
    private OrderEntityTransformer transformer;

    @Test
    public void testTransformToEntityApple() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);
        order.setSale(false);
        OrderEntity orderEntity = transformer.transformToEntity(order);
        assertThat(orderEntity.getProductList().get(0).getPrice()).isEqualTo(0.6);
    }

    @Test
    public void testTransformToEntityOrange() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("orange");
        product.setQuantity(3);
        products.add(product);
        order.setProducts(products);
        order.setSale(false);
        OrderEntity orderEntity = transformer.transformToEntity(order);
        assertThat(orderEntity.getProductList().get(0).getPrice()).isEqualTo(0.25);
    }

    @Test
    public void testTransformFromEntity() {
        OrderEntity orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = ProductEntity.builder().productId(1).name("apple").quantity(2).price(0.6).build();
        productEntities.add(productEntity);
        orderEntity.setProductList(productEntities);
        orderEntity.setSale("N");
        orderEntity.setTotal(1.2);
        orderEntity.setOrderId(1);
        Order order = transformer.transformFromEntity(orderEntity);
        assertThat(order.getProducts().get(0).getProductId()).isEqualTo("1");
    }
}

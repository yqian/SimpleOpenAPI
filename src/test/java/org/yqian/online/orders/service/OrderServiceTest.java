package org.yqian.online.orders.service;

import org.yqian.online.orders.codegen.model.Order;
import org.yqian.online.orders.codegen.model.Product;
import org.yqian.online.orders.entity.OrderEntity;
import org.yqian.online.orders.entity.ProductEntity;
import org.yqian.online.orders.exception.OrderNotFoundException;
import org.yqian.online.orders.repository.OrderRepository;
import org.yqian.online.orders.transform.OrderEntityTransformer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    @Mock
    private OrderEntityTransformer mockTransformer;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testPlaceNormalOrder() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);
        order.setSale(false);
        order.setTotal(1.2);

        OrderEntity orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = ProductEntity.builder().name("apple").productId(1).quantity(2).price(0.6).build();
        productEntities.add(productEntity);
        orderEntity.setProductList(productEntities);

        given(mockTransformer.transformToEntity(order)).willReturn(orderEntity);
        given(mockRepository.save(orderEntity)).willReturn(orderEntity);
        given(mockTransformer.transformFromEntity(orderEntity)).willReturn(order);
        Order result = orderService.placeOrder(order);
        assertThat(result.getTotal()).isEqualTo(1.2);
    }

    @Test
    public void testPlaceSaleOrderApple() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);
        order.setSale(true);
        order.setTotal(0.6);

        OrderEntity orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = ProductEntity.builder().name("apple").productId(1).quantity(2).price(0.6).build();
        productEntities.add(productEntity);
        orderEntity.setProductList(productEntities);

        given(mockTransformer.transformToEntity(order)).willReturn(orderEntity);
        given(mockRepository.save(orderEntity)).willReturn(orderEntity);
        given(mockTransformer.transformFromEntity(orderEntity)).willReturn(order);
        Order result = orderService.placeOrder(order);
        assertThat(result.getTotal()).isEqualTo(0.6);
    }

    @Test
    public void testPlaceSaleOrderOrange() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("orange");
        product.setQuantity(3);
        products.add(product);
        order.setProducts(products);
        order.setSale(true);
        order.setTotal(0.5);

        OrderEntity orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = ProductEntity.builder().name("orange").productId(2).quantity(3).price(0.25).build();
        productEntities.add(productEntity);
        orderEntity.setProductList(productEntities);

        given(mockTransformer.transformToEntity(order)).willReturn(orderEntity);
        given(mockRepository.save(orderEntity)).willReturn(orderEntity);
        given(mockTransformer.transformFromEntity(orderEntity)).willReturn(order);
        Order result = orderService.placeOrder(order);
        assertThat(result.getTotal()).isEqualTo(0.5);
    }

    @Test
    public void testGetOrderById() throws OrderNotFoundException {
        Order order = new Order();
        order.setTotal(1.2);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTotal(1.2);

        given(mockRepository.findById(1)).willReturn(Optional.of(orderEntity));
        given(mockTransformer.transformFromEntity(orderEntity)).willReturn(order);
        assertThat(orderService.getOrderById(1).getTotal()).isEqualTo(1.2);
    }

    @Test
    public void testGetOrderByIdNotFound() {
        given(mockRepository.findById(1)).willReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.getOrderById(1)).isInstanceOfAny(OrderNotFoundException.class);
    }
}

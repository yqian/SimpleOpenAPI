package edu.yqian.online.orders.service;

import edu.yqian.online.orders.codegen.model.Order;
import edu.yqian.online.orders.codegen.model.Product;
import edu.yqian.online.orders.entity.OrderEntity;
import edu.yqian.online.orders.entity.ProductEntity;
import edu.yqian.online.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    private OrderEntity orderEntity;

    @BeforeAll
    public void setup() {
        order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);
        order.setTotal(new BigDecimal(1.2));

        orderEntity = new OrderEntity();
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity = ProductEntity.builder().name("apple").productId(1).quantity(2).price(0.6).build();
        productEntities.add(productEntity);
        orderEntity.setProductList(productEntities);
    }

    @Test
    public void testNormalOrder() {
        given(mockRepository.save(orderEntity)).willReturn(orderEntity);
        Order result = orderService.normalOrder(order);
        assertThat(result.getTotal()).isEqualTo(BigDecimal.valueOf(1.2));
    }
}

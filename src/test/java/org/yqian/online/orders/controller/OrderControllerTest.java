package org.yqian.online.orders.controller;

import org.yqian.online.orders.codegen.model.Order;
import org.yqian.online.orders.codegen.model.Product;
import org.yqian.online.orders.exception.OrderNotFoundException;
import org.yqian.online.orders.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService mockService;

    private Order order;

    @BeforeAll
    public void setup() {
        order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);
        order.setOrderId("1");
        order.setSale(false);
        order.setTotal(1.2);
    }
    @Test
    public void testListOrderById() throws Exception {
        given(mockService.getOrderById(1)).willReturn(order);
        mockMvc.perform(get("/orders/{orderId}", 123))
                .andExpect(status().isOk());
    }

    @Test
    public void testListOrderByIdNotFound() throws Exception {
        given(mockService.getOrderById(123)).willThrow(new OrderNotFoundException());
        mockMvc.perform(get("/orders/{orderId}", 123))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNormalOrder() throws Exception {
        String orderJson = "{\"orderId\":\"1\",\"products\":[{\"productId\":\"1\",\"name\":\"apple\",\"price\":0.6,\"quantity\":2}],\"total\":1.2}";
        given(mockService.placeOrder(order)).willReturn(order);
        mockMvc.perform(post("/orders").content(orderJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaleOrder() throws Exception {
        String orderJson = "{ \"sale\" : true, \"total\" : \"0\", \"orderId\" : \"1\", \"products\" : [ { \"quantity\" : 2, \"productId\" : \"1\", \"price\" : \"0.6\", \"name\" : \"apple\" }, { \"quantity\" : 3, \"productId\" : \"2\", \"price\" : \"0.25\", \"name\" : \"orange\" } ] }";
        given(mockService.placeOrder(new Order())).willReturn(new Order());
        mockMvc.perform(post("/orders").content(orderJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}

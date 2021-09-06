package edu.yqian.online.orders.controller;

import edu.yqian.online.orders.codegen.model.Order;
import edu.yqian.online.orders.codegen.model.Product;
import edu.yqian.online.orders.exception.OrderNotFoundException;
import edu.yqian.online.orders.service.OrderService;
import org.junit.jupiter.api.Test;
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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService mockService;

    @Test
    public void testListOrderById() throws Exception {
        given(mockService.getOrderById(123)).willReturn(new Order());
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
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setName("apple");
        product.setQuantity(2);
        products.add(product);
        order.setProducts(products);

        String orderJson = "{\"orderId\":\"1\",\"products\":[{\"productId\":\"1\",\"name\":\"apple\",\"price\":0.6,\"quantity\":2}],\"total\":1.2}";
        given(mockService.normalOrder(order)).willReturn(new Order());
        mockMvc.perform(post("/orders").content(order.toString()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());
    }

    @Test
    public void testOfferOrder() throws Exception {
        given(mockService.normalOrder(new Order())).willReturn(new Order());
        mockMvc.perform(post("/orders").content((new Order()).toString()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());
    }
}

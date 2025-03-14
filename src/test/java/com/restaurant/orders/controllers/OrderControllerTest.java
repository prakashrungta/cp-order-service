package com.restaurant.orders.controllers;

import com.restaurant.orders.entities.Order;
import com.restaurant.orders.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setDescription("this is a test order");
        order.setPrice(0.0);
        // Set the name field
        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"this is a test order\",\"price\":0.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"description\":\"this is a test order\",\"price\":0.0}"));
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testCreateOrderNegativePrice() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"this is a test order\",\"price\":-1.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"user", "admin"})
    public void testGetOrders() throws Exception {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"));
    }
}
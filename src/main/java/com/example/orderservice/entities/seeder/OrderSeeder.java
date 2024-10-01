package com.example.orderservice.entities.seeder;

import com.example.orderservice.dto.common.ProductDTO;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.entities.*;
import com.example.orderservice.repositories.*;
import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.service.ProductServiceClientImpl;
import com.example.orderservice.service.UserServiceClientImpl;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

//@Component
public class OrderSeeder implements CommandLineRunner {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    UserServiceClientImpl userServiceClient;
    ProductServiceClientImpl productServiceClient;
    Faker faker = new Faker();

    public OrderSeeder(OrderRepository orderRepository,
                       OrderDetailRepository orderDetailRepository,
                       UserServiceClientImpl userServiceClient,
                       ProductServiceClientImpl productServiceClient) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userServiceClient = userServiceClient;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public void run(String... args) throws Exception {
        if (orderRepository.count() == 0) {
            createOrders();
        }
    }

    private void createOrders() {
        List<Order> orders = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        boolean existProduct = false;

        for (int i = 1; i <= 2; i++) {
            Order order = new Order();
            long total = 0;
            int userId = faker.number().numberBetween(3, 3);
            int orderDetailNumber = faker.number().numberBetween(1, 2);

            System.out.println(order.getId());
            order.setStatus(OrderSimpleStatus.PENDING);
            order.setUserId(userServiceClient.getUserById((long) userId).getData().getId());
            for (int j = 0; j < orderDetailNumber; j++) {
                int productId = faker.number().numberBetween(1, 5);
                for (OrderDetail od : orderDetails) {
                    if (od.getId().getProductId() == productId && od.getOrder().getUserId() == userId) {
                        existProduct = true;
                        break;
                    }
                }
                if (existProduct) {
                    j--;
                    existProduct = false;
                    continue;
                }
                OrderDetail orderDetail = new OrderDetail();
                ApiResponse<ProductDTO> product = productServiceClient.getProductById((long) productId);
                orderDetail.setId(new OrderDetailId(order.getId(), product.getData().getProductId()));
                int quantity = faker.number().numberBetween(1, 5);
                orderDetail.setOrder(order);
                orderDetail.setQuantity(quantity);
                long unitPrice = product.getData().getPrice().longValue() * quantity;
                orderDetail.setUnitPrice(new BigDecimal(unitPrice));
                total += unitPrice;
                orderDetails.add(orderDetail);
            }
            order.setTotalPrice(new BigDecimal(total));
            orders.add(order);
            orderRepository.saveAll(orders);
            orderDetailRepository.saveAll(orderDetails);
            orders.clear();
            orderDetails.clear();
        }
    }
}

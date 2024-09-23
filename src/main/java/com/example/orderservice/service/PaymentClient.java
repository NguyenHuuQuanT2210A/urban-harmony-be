package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "https://techwiz5-payment-service-gyc4d3cpbagyg8eh.eastasia-01.azurewebsites.net/api/v1/payment",
        configuration = AuthenticationRequestInterceptor.class)
public interface PaymentClient {
    @PostMapping("/create_payment")
    String creatPayment(@RequestBody PaymentRequest request);
}

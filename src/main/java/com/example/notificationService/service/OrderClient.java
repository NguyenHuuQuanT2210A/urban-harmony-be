package com.example.notificationService.service;

import com.example.notificationService.common.enums.OrderSimpleStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "https://techwiz5-order-service-dchugggwh5g9hjdx.eastasia-01.azurewebsites.net/api/v1/orders")
public interface OrderClient {
    @PutMapping("/changeStatus/{id}")
    String changeStatus(@PathVariable String id, @RequestParam OrderSimpleStatus status);
}
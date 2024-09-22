package com.example.orderservice.service;

import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.request.UserAndProductId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "cart-service", url = "https://techwiz5-user-service-hbereff9dmexc6er.eastasia-01.azurewebsites.net/api/v1/cart",
        configuration = { AuthenticationRequestInterceptor.class })
public interface CartClient {
    @DeleteMapping("/ids")
    void deleteByIds(@RequestBody List<UserAndProductId> ids);
}

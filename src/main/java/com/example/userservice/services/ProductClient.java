package com.example.userservice.services;

import com.example.userservice.configs.AuthenticationRequestInterceptor;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", url = "https://techwiz-product-service-fpd5bedth9ckdgay.eastasia-01.azurewebsites.net/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductClient {
    @GetMapping("/id/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id);
}

package com.example.orderservice.service;

import com.example.orderservice.dto.common.ProductDTO;
import com.example.orderservice.config.AuthenticationRequestInterceptor;
import com.example.orderservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service", url = "https://techwiz-product-service-fpd5bedth9ckdgay.eastasia-01.azurewebsites.net/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProductServiceClient {

    @GetMapping("/id/{id}")
    ApiResponse<ProductDTO> getProductById(@PathVariable("id") Long id);

    @PostMapping("/list")
    ApiResponse<List<ProductDTO>> getProductsByIds(@RequestBody Set<Long> productIds);

    @PutMapping("/updateQuantity/{id}")
    void updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity);
}

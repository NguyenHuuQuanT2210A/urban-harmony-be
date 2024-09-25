package com.example.userservice.services;

import com.example.userservice.configs.AuthenticationRequestInterceptor;
import com.example.userservice.dtos.request.ProductRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "product-service", url = "https://techwiz-product-service-fpd5bedth9ckdgay.eastasia-01.azurewebsites.net/api/v1/products",
        configuration = { AuthenticationRequestInterceptor.class })
public interface FeignClientService {

    @GetMapping("/getAll")
    String getAllProducts();

    @GetMapping("/id/{id}")
    ProductRequest getProductById(@PathVariable("id") Long id);

    @PostMapping()
    String createProduct(ProductRequest product);
}
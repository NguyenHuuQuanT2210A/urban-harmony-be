package com.example.userservice.services;

import com.example.userservice.dtos.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<String> getAllProducts() {
        return webClientBuilder.build()
                .get()
                .uri("https://techwiz-product-service-fpd5bedth9ckdgay.eastasia-01.azurewebsites.net/api/v1/products")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> createProduct(ProductRequest product) {
        return webClientBuilder.build()
                .post()
                .uri("https://techwiz-product-service-fpd5bedth9ckdgay.eastasia-01.azurewebsites.net/api/v1/products")
                .body(Mono.just(product), ProductRequest.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
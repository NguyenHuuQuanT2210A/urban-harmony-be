package com.example.designgalleryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DesignGalleryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesignGalleryServiceApplication.class, args);
    }

}

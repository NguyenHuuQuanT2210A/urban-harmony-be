package com.example.orderservice.dto.common;

import com.example.orderservice.dto.CategoryDTO;
import com.example.orderservice.dto.ProductImageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;

    private CategoryDTO category;

    private Set<ProductImageDTO> images;

    private Integer stockQuantity;

    private String manufacturer;

    private String size;

    private String weight;

    private Long soldQuantity;
}

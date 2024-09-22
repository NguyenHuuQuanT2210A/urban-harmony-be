package com.example.orderservice.dto.common;

import com.example.orderservice.entities.OrderDetailId;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @EmbeddedId
    private OrderDetailId id = new OrderDetailId();
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
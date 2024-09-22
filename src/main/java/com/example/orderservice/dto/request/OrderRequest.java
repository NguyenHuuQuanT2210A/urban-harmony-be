package com.example.orderservice.dto.request;

import com.example.orderservice.enums.OrderSimpleStatus;
import com.example.orderservice.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
    private String id;
    private Long userId;

    private Long addressOrderId;
    private String note;
    private String paymentMethod;

    private BigDecimal totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderSimpleStatus status;

    private String createdAt;
    private String updatedAt;
    private UserDTO user;
    private Set<CartItemRequest> cartItems;
}

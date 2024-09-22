package com.example.notificationService.common.dto.response;

import com.example.notificationService.common.dto.UserDTO;
import com.example.notificationService.common.enums.OrderSimpleStatus;
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
public class OrderResponse {
    private String id;
    private Long userId;

    private String username;
    private String addressRegion;
    private String addressDetail;
    private String phone;
    private String email;
    private String note;
    private String paymentMethod;

    private BigDecimal totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderSimpleStatus status;

    private String createdAt;
    private String updatedAt;
    private UserDTO user;
    private Set<OrderDetailResponse> orderDetails;
}

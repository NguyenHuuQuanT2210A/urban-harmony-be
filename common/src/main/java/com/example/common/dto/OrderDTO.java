package com.example.common.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // custom string id
    private String id;
    private Long userId;
    private BigDecimal totalPrice;

    private String username;
    private String addressRegion;
    private String addressDetail;
    private String phone;
    private String email;
    private String note;
    private String paymentMethod;

    private String status;
    private String createdAt;
    private String updatedAt;
    private UserDTO user;
    private Set<OrderDetailDTO> orderDetails;
}

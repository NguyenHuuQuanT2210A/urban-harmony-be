package com.example.orderservice.dto.request;

import com.example.common.enums.OrderSimpleStatus;
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
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    // custom string id
    private String id;
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String country;
    private String postalCode;
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

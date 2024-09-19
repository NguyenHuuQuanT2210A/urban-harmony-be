package com.example.userservice.dtos.response;

import com.example.userservice.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressOrderResponse {
    private Long id;
    private String username;
    private String phone;
    private String addressRegion;
    private String addressDetail;
    private String isDefault;
}

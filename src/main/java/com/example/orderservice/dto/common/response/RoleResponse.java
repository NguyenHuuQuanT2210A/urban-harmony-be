package com.example.orderservice.dto.common.response;

import com.example.orderservice.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private long id;
    @Enumerated(EnumType.STRING)
    private ERole name;
}

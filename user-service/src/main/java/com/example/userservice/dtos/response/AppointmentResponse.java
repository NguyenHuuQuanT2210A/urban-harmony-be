package com.example.userservice.dtos.response;

import com.example.userservice.statics.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
    private AppointmentStatus status;
    private Long designerId;
    private Long userId;
}

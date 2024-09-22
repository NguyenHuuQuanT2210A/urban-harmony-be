package com.example.userservice.dtos.request;

import com.example.userservice.statics.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private LocalDateTime datetimeStart;
    private AppointmentStatus status;
    private String appointmentUrl;
    private Long designerId;
    private Long userId;
}

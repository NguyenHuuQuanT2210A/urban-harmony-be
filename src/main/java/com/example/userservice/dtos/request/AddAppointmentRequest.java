package com.example.userservice.dtos.request;

import com.example.userservice.statics.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAppointmentRequest {
    private LocalDateTime datetimeStart;
    private AppointmentStatus status;
    private Long designerId;
    private Long userId;
}

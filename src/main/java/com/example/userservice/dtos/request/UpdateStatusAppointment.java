package com.example.userservice.dtos.request;

import com.example.userservice.statics.enums.AppointmentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusAppointment {
    AppointmentStatus status;
    Long userId;
}

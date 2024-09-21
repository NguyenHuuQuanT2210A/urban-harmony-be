package com.example.userservice.dtos.response;

import com.example.userservice.dtos.UserDTO;
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
    private String appointmentUrl;
    private UserDTO designer;
    private UserDTO user;
}

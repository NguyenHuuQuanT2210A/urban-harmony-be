package com.example.userservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAppointmentRequest {
    private Integer rateStar;
    private String comment;
    private Long appointmentId;
    private Long userId;
}

package com.example.userservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAppointmentResponse {
    private Long id;
    private Integer rateStar;
    private String comment;
    private Long appointmentId;
    private Long userId;
}

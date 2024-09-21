package com.example.userservice.services;


import com.example.userservice.dtos.request.ReviewAppointmentRequest;
import com.example.userservice.dtos.response.ReviewAppointmentResponse;

import java.util.List;

public interface ReviewAppointmentService {
    ReviewAppointmentResponse findById(Long id);
    List<ReviewAppointmentResponse> findByUserId(Long userId);
    ReviewAppointmentResponse findByAppointmentId(Long appointmentId);
    ReviewAppointmentResponse createReviewAppointment(ReviewAppointmentRequest request);
    ReviewAppointmentResponse updateReviewAppointment(Long id, ReviewAppointmentRequest request);
    void deleteReviewAppointment(Long id);
}

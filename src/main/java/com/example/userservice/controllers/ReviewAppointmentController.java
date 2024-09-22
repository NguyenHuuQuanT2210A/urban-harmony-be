package com.example.userservice.controllers;

import com.example.userservice.dtos.request.ReviewAppointmentRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.ReviewAppointmentResponse;
import com.example.userservice.services.ReviewAppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review_appointments")
@Tag(name = "ReviewAppointment", description = "ReviewAppointment Controller")
public class ReviewAppointmentController {
    private final ReviewAppointmentService reviewAppointmentService;

    @GetMapping("/id/{id}")
    ApiResponse<ReviewAppointmentResponse> getReviewAppointmentById(@PathVariable Long id) {
        return ApiResponse.<ReviewAppointmentResponse>builder()
                .message("Get ReviewAppointment by Id")
                .data(reviewAppointmentService.findById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<ReviewAppointmentResponse>> getReviewAppointmentByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<ReviewAppointmentResponse>>builder()
                .message("Get ReviewAppointment by User Id")
                .data(reviewAppointmentService.findByUserId(userId))
                .build();
    }

    @GetMapping("/appointment/{appointmentId}")
    ApiResponse<ReviewAppointmentResponse> getReviewAppointmentByProductId(@PathVariable Long appointmentId) {
        return ApiResponse.<ReviewAppointmentResponse>builder()
                .message("Get ReviewAppointment by Appointment Id")
                .data(reviewAppointmentService.findByAppointmentId(appointmentId))
                .build();
    }

    @PostMapping
    ApiResponse<String> createReviewAppointment(@RequestBody ReviewAppointmentRequest request) {
        reviewAppointmentService.createReviewAppointment(request);
        return ApiResponse.<String>builder()
                .message("Create ReviewAppointment success")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ReviewAppointmentResponse> updateReviewAppointment(@PathVariable Long id, @RequestBody ReviewAppointmentRequest request) {
        return ApiResponse.<ReviewAppointmentResponse>builder()
                .message("Updated ReviewAppointment")
                .data(reviewAppointmentService.updateReviewAppointment(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteReviewAppointment(@PathVariable Long id) {
        reviewAppointmentService.deleteReviewAppointment(id);
        return ApiResponse.<String>builder()
                .message("Deleted ReviewAppointment Successfully!")
                .build();
    }

}

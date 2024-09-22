package com.example.userservice.controllers;

import com.example.userservice.dtos.request.AppointmentRequest;
import com.example.userservice.dtos.response.AppointmentResponse;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.services.AppointmentService;
import com.example.userservice.statics.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "Address Order", description = "Address Order Controller")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<AppointmentResponse>> getAllAppointmentByUserId(@PathVariable Long userId,
                                                                            @RequestParam(name = "page") int page,
                                                                            @RequestParam(name = "limit") int limit) {
        return ApiResponse.<Page<AppointmentResponse>>builder()
                .message("Get all Appointment By UserId")
                .data(appointmentService.getAppointmentsByUserId(userId, PageRequest.of(page - 1, limit)))
                .build();
    }

    @GetMapping("/designer/{designerId}")
    public ApiResponse<Page<AppointmentResponse>> getAllAppointmentByDesignerId(@PathVariable Long designerId,
                                                                            @RequestParam(name = "page") int page,
                                                                            @RequestParam(name = "limit") int limit) {
        return ApiResponse.<Page<AppointmentResponse>>builder()
                .message("Get all Appointment By Designer Id")
                .data(appointmentService.getAppointmentsByDesignId(designerId, PageRequest.of(page - 1, limit)))
                .build();
    }

    @GetMapping("/day")
    public ApiResponse<List<AppointmentResponse>> getAllAppointmentsByDay(@RequestParam LocalDateTime date, @RequestParam Long designerId) {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .message("Get All Appointments By Day")
                .data(appointmentService.getAllAppointmentsByDay(date, designerId))
                .build();
    }

    @GetMapping("/id/{id}")
    public ApiResponse<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Get Appointment by Id")
                .data(appointmentService.getAppointmentById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<AppointmentResponse> addAppointment(@RequestBody AppointmentRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Create Appointment")
                .data(appointmentService.addAppointment(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AppointmentResponse> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Update Appointment")
                .data(appointmentService.updateAppointment(id, request))
                .build();
    }

    @PutMapping("/updateStatus/{id}")
    public ApiResponse<AppointmentResponse> updateStatusAppointment(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return ApiResponse.<AppointmentResponse>builder()
                .message("Update Appointment")
                .data(appointmentService.updateStatusAppointment(id, status))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ApiResponse.<String>builder()
                .message("Delete Appointment Successfully")
                .build();
    }
}

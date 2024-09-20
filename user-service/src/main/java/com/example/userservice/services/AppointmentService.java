package com.example.userservice.services;

import com.example.userservice.dtos.request.AppointmentRequest;
import com.example.userservice.dtos.response.AppointmentResponse;
import com.example.userservice.statics.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {
    Page<AppointmentResponse> getAppointmentsByUserId(Long userId, Pageable pageable);
    Page<AppointmentResponse> getAppointmentsByDesignId(Long designId, Pageable pageable);
    AppointmentResponse getAppointmentById(Long id);
    AppointmentResponse addAppointment(AppointmentRequest request);
    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);
    AppointmentResponse updateStatusAppointment(Long id, AppointmentStatus status);
    void deleteAppointment(Long id);
}

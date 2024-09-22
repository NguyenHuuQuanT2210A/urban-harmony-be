package com.example.userservice.services;

import com.example.userservice.dtos.request.AppointmentRequest;
import com.example.userservice.dtos.request.UpdateStatusAppointment;
import com.example.userservice.dtos.response.AppointmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Page<AppointmentResponse> getAppointmentsByUserId(Long userId, Pageable pageable);
    Page<AppointmentResponse> getAppointmentsByDesignId(Long designId, Pageable pageable);
    AppointmentResponse getAppointmentById(Long id);
    AppointmentResponse addAppointment(AppointmentRequest request);
    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);
    AppointmentResponse updateStatusAppointment(Long id, UpdateStatusAppointment updateStatusAppointment);
    List<AppointmentResponse> getAllAppointmentsByDay(LocalDateTime date, Long designerId);
    void deleteAppointment(Long id);
}

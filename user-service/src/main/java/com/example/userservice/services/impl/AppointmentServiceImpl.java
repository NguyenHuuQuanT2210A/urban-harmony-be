package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.AppointmentRequest;
import com.example.userservice.dtos.response.AppointmentResponse;
import com.example.userservice.entities.Appointment;
import com.example.userservice.entities.Role;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.AppointmentMapper;
import com.example.userservice.repositories.AppointmentRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.AppointmentService;
import com.example.userservice.services.AppointmentService;
import com.example.userservice.statics.enums.AppointmentStatus;
import com.example.userservice.statics.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;

    @Override
    public Page<AppointmentResponse> getAppointmentsByUserId(Long userId, Pageable pageable) {
        return appointmentRepository.findAllByUserId(userId, pageable).map(appointmentMapper::toAppointmentResponse);
    }

    @Override
    public Page<AppointmentResponse> getAppointmentsByDesignId(Long designId, Pageable pageable) {
        return appointmentRepository.findAllByDesignerId(designId, pageable).map(appointmentMapper::toAppointmentResponse);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentMapper.toAppointmentResponse(findAppointmentById(id));
    }

    @Override
    public AppointmentResponse addAppointment(AppointmentRequest request) {
        var designer = userRepository.findById(request.getDesignerId()).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        boolean hasDesignerRole = designer.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_DESIGNER));

        if (!hasDesignerRole) {
            throw new CustomException("User is not a designer", HttpStatus.BAD_REQUEST);
        }
        var appointment = appointmentMapper.toAppointment(request);
        appointment.setDatetimeEnd(request.getDatetimeStart().plusHours(1));
        appointment.setStatus(AppointmentStatus.CREATED);
        appointment.setDesigner(designer);
        appointment.setUser(null);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user =userRepository.findById(request.getUserId()).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        }
        var appointment = findAppointmentById(id);
        if (Objects.nonNull(request.getDatetimeStart())) {
            appointment.setDatetimeStart(request.getDatetimeStart());
            appointment.setDatetimeEnd(request.getDatetimeStart().plusHours(1));
        }
        if (Objects.nonNull(request.getDesignerId())) {
            appointment.setDesigner(appointment.getDesigner());
        }
        if (Objects.nonNull(request.getUserId())) {
            appointment.setUser(user);
        }
        if (Objects.nonNull(request.getStatus())) {
            if (request.getStatus() == AppointmentStatus.CANCELLED) {
                appointment.setStatus(AppointmentStatus.CREATED);
                appointment.setUser(null);
            }else {
                appointment.setStatus(request.getStatus());
            }
        }
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateStatusAppointment(Long id, AppointmentStatus status) {
        var appointment = findAppointmentById(id);
        if (status == null) {
            throw new CustomException("Status is required", HttpStatus.BAD_REQUEST);
        }

        if (status == AppointmentStatus.CANCELLED) {
            appointment.setStatus(AppointmentStatus.CREATED);
            appointment.setUser(null);
        }
        appointment.setStatus(status);
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new CustomException("Appointment not found", HttpStatus.NOT_FOUND));
    }
}

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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        if (!isDesigner(designer)) {
            throw new CustomException("User is not a designer", HttpStatus.BAD_REQUEST);
        }
        var appointment = appointmentMapper.toAppointment(request);
        appointment.setDatetimeEnd(request.getDatetimeStart().plusHours(1));
        appointment.setStatus(AppointmentStatus.AVAILABLE);
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
        appointmentMapper.updateAppointment(appointment, request);
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
        if (Objects.nonNull(request.getAppointmentUrl())) {
            appointment.setAppointmentUrl(request.getAppointmentUrl());
        }
        if (Objects.nonNull(request.getStatus())) {
            appointment.setStatus(request.getStatus());
        }
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateStatusAppointment(Long id, AppointmentStatus status, Long userId) {
        User user = null;
        if (userId != null) {
            user =userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        }
        var appointment = findAppointmentById(id);
        if (status == null) {
            throw new CustomException("Status is required", HttpStatus.BAD_REQUEST);
        }

        if (status == AppointmentStatus.AVAILABLE) {
            appointment.setStatus(AppointmentStatus.AVAILABLE);
            appointment.setUser(user);
        }else {
            appointment.setStatus(AppointmentStatus.UNAVAILABLE);
            appointment.setUser(user);
        }
        appointment.setStatus(status);
        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponse> getAllAppointmentsByDay(LocalDateTime date, Long designerId) {
        var designer = userRepository.findById(designerId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        if (!isDesigner(designer)) {
            throw new CustomException("User is not a designer", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);

        // Lấy danh sách appointment trong khoảng ngày đã chọn
        List<Appointment> appointments = appointmentRepository.findAllByDatetimeStartBetweenAndDesignerId(startOfDay, endOfDay, designerId);

        // Thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra và cập nhật trạng thái
        appointments.forEach(appointment -> {
            LocalDateTime appointmentStartTime = appointment.getDatetimeStart();

            // Nếu thời gian hiện tại nằm trong khoảng trước 1 tiếng hoặc sau thời gian bắt đầu của cuộc hẹn
            if (appointmentStartTime.isBefore(now.plusHours(1)) || now.isAfter(appointmentStartTime)) {
                appointment.setStatus(AppointmentStatus.UNAVAILABLE); // Đặt trạng thái thành UNAVAILABLE
                appointmentRepository.save(appointment); // Cập nhật lại trong database
            }
        });

        // Trả về danh sách AppointmentResponse
        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new CustomException("Appointment not found", HttpStatus.NOT_FOUND));
    }

    private boolean isDesigner(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_DESIGNER));
    }
}

package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.ReviewAppointmentRequest;
import com.example.userservice.dtos.response.ReviewAppointmentResponse;
import com.example.userservice.entities.ReviewAppointment;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.ReviewAppointmentMapper;
import com.example.userservice.repositories.AppointmentRepository;
import com.example.userservice.repositories.ReviewAppointmentRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.ReviewAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewAppointmentServiceImpl implements ReviewAppointmentService {
    private final ReviewAppointmentRepository reviewAppointmentRepository;
    private final ReviewAppointmentMapper reviewAppointmentMapper;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;


    @Override
    public ReviewAppointmentResponse findById(Long id) {
        return reviewAppointmentMapper.toReviewAppointmentResponse(getReviewAppointmentById(id));
    }

    @Override
    public ReviewAppointmentResponse findByAppointmentId(Long appointmentId) {
        appointmentRepository.findById(appointmentId).orElseThrow(() -> new CustomException("Appointment not found", HttpStatus.NOT_FOUND));
        return reviewAppointmentMapper.toReviewAppointmentResponse(reviewAppointmentRepository.findByAppointmentId(appointmentId));
    }

    @Override
    @Transactional
    public List<ReviewAppointmentResponse> findByUserId(Long userId) {
        List<ReviewAppointmentResponse> reviewAppointmentResponses = new ArrayList<>();

        List<ReviewAppointment> reviewAppointments = reviewAppointmentRepository.findByUserId(userId);
        for (ReviewAppointment reviewAppointment : reviewAppointments) {
            reviewAppointmentResponses.add(reviewAppointmentMapper.toReviewAppointmentResponse(reviewAppointment));
        }

        return reviewAppointmentResponses;
    }

    @Override
    public ReviewAppointmentResponse createReviewAppointment(ReviewAppointmentRequest request) {
        try {
            ReviewAppointment reviewAppointment = reviewAppointmentMapper.toReviewAppointment(request);
            reviewAppointment.setAppointment(appointmentRepository.findById(request.getAppointmentId()).orElseThrow(() -> new CustomException("Appointment not found", HttpStatus.NOT_FOUND)));
            reviewAppointment.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND)));
            return reviewAppointmentMapper.toReviewAppointmentResponse(reviewAppointmentRepository.save(reviewAppointment));
        }catch (Exception e) {
            throw new CustomException("error while create reviewAppointment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ReviewAppointmentResponse updateReviewAppointment(Long id, ReviewAppointmentRequest request) {
        ReviewAppointment reviewAppointmentUpdate = getReviewAppointmentById(id);
        reviewAppointmentMapper.updateReviewAppointment(reviewAppointmentUpdate, request);
        if (request.getAppointmentId() != null || request.getUserId() != null) {
            reviewAppointmentUpdate.setAppointment(reviewAppointmentUpdate.getAppointment());
            reviewAppointmentUpdate.setUser(reviewAppointmentUpdate.getUser());
        }
        if (request.getRateStar() != null) {
            reviewAppointmentUpdate.setRateStar(request.getRateStar());
        }
        if (request.getComment() != null) {
            reviewAppointmentUpdate.setComment(request.getComment());
        }
        try {
            return reviewAppointmentMapper.toReviewAppointmentResponse(reviewAppointmentRepository.save(reviewAppointmentUpdate));
        }catch (Exception e) {
            throw new CustomException("error while updating reviewAppointment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteReviewAppointment(Long id) {
        reviewAppointmentRepository.deleteById(id);
    }

    private ReviewAppointment getReviewAppointmentById(Long id){
        return reviewAppointmentRepository.findById(id).orElseThrow(() -> new CustomException("ReviewAppointment not found", HttpStatus.NOT_FOUND));
    }
}

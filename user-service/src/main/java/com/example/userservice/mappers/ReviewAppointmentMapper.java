package com.example.userservice.mappers;

import com.example.userservice.dtos.request.ReviewAppointmentRequest;
import com.example.userservice.dtos.response.ReviewAppointmentResponse;
import com.example.userservice.entities.ReviewAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewAppointmentMapper {
    ReviewAppointmentMapper INSTANCE = Mappers.getMapper(ReviewAppointmentMapper.class);

    ReviewAppointment toReviewAppointment(ReviewAppointmentRequest request);
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "user.id", target = "userId")
    ReviewAppointmentResponse toReviewAppointmentResponse(ReviewAppointment reviewAppointment);
    void updateReviewAppointment(@MappingTarget ReviewAppointment reviewAppointment, ReviewAppointmentRequest request);
}
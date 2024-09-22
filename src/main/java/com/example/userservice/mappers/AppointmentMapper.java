package com.example.userservice.mappers;

import com.example.userservice.dtos.request.AddAppointmentRequest;
import com.example.userservice.dtos.request.AppointmentRequest;
import com.example.userservice.dtos.response.AppointmentResponse;
import com.example.userservice.entities.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    Appointment toAppointment(AddAppointmentRequest request);

    @Mapping(source = "designer", target = "designer")
    @Mapping(source = "user", target = "user")
    AppointmentResponse toAppointmentResponse(Appointment appointment);
    void updateAppointment(@MappingTarget Appointment appointment, AppointmentRequest request);
    Appointment appointmentResponseToAppointment(AppointmentResponse response);

}
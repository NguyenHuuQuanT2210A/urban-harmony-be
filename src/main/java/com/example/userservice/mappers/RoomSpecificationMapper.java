package com.example.userservice.mappers;

import com.example.userservice.dtos.request.AddressOrderRequest;
import com.example.userservice.dtos.request.RoomSpecificationRequest;
import com.example.userservice.dtos.response.AddressOrderResponse;
import com.example.userservice.dtos.response.RoomSpecificationResponse;
import com.example.userservice.entities.AddressOrder;
import com.example.userservice.entities.RoomSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomSpecificationMapper {
    RoomSpecificationMapper INSTANCE = Mappers.getMapper(RoomSpecificationMapper.class);

    @Mapping(target = "imageList", ignore = true)
    RoomSpecification toRoomSpecification(RoomSpecificationRequest request);

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "imageList", ignore = true)
    RoomSpecificationResponse toRoomSpecificationResponse(RoomSpecification roomSpecification);
    @Mapping(target = "imageList", ignore = true)
    void updateRoomSpecification(@MappingTarget RoomSpecification roomSpecification, RoomSpecificationRequest request);
}
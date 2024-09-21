package com.example.userservice.mappers;

import com.example.userservice.dtos.request.DesignerProfileRequest;
import com.example.userservice.dtos.request.ImageDesignDesignerRequest;
import com.example.userservice.dtos.response.DesignerProfileResponse;
import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import com.example.userservice.entities.DesignerProfile;
import com.example.userservice.entities.ImageDesignDesigner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DesignerProfileMapper {
    DesignerProfileMapper INSTANCE = Mappers.getMapper(DesignerProfileMapper.class);

    DesignerProfile toDesignerProfile(DesignerProfileRequest request);
    @Mapping(source = "user.id", target = "userId")
    DesignerProfileResponse toDesignerProfileResponse(DesignerProfile designerProfile);
    void updateDesignerProfile(@MappingTarget DesignerProfile designerProfile, DesignerProfileRequest request);
}
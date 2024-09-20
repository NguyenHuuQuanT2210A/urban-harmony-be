package com.example.userservice.mappers;

import com.example.userservice.dtos.request.ImageDesignDesignerRequest;
import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import com.example.userservice.entities.ImageDesignDesigner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageDesignDesignerMapper {
    ImageDesignDesignerMapper INSTANCE = Mappers.getMapper(ImageDesignDesignerMapper.class);

    ImageDesignDesigner toImageDesignDesigner(ImageDesignDesignerRequest request);

    ImageDesignDesignerResponse toImageDesignDesignerResponse(ImageDesignDesigner imageDesignDesigner);
    void updateImageDesignDesigner(@MappingTarget ImageDesignDesigner imageDesignDesigner, ImageDesignDesignerRequest request);
}
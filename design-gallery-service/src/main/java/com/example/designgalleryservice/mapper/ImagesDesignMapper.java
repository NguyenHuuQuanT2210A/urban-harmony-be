package com.example.designgalleryservice.mapper;

import com.example.designgalleryservice.dto.request.ImagesDesignRequest;
import com.example.designgalleryservice.dto.response.ImagesDesignResponse;
import com.example.designgalleryservice.entities.ImagesDesign;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImagesDesignMapper {
    ImagesDesignMapper INSTANCE = Mappers.getMapper(ImagesDesignMapper.class);
    ImagesDesign toImagesDesign(ImagesDesignRequest request);
    ImagesDesignResponse toImagesDesignResponse(ImagesDesign imagesDesign);
    void updatedImagesDesign(@MappingTarget ImagesDesign imagesDesign, ImagesDesignRequest request);
}

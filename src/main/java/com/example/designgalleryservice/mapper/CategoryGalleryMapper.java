package com.example.designgalleryservice.mapper;

import com.example.designgalleryservice.dto.request.CategoryGalleryRequest;
import com.example.designgalleryservice.dto.response.CategoryGalleryResponse;
import com.example.designgalleryservice.entities.CategoryGallery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryGalleryMapper {
    CategoryGalleryMapper INSTANCE = Mappers.getMapper(CategoryGalleryMapper.class);
    CategoryGallery categoryGalleryRequesttoCategoryGallery(CategoryGalleryRequest request);
    CategoryGallery categoryGalleryResponsetoCategoryGallery(CategoryGalleryResponse response);
    CategoryGalleryResponse toCategoryGalleryResponse(CategoryGallery categoryGallery);
    void updatedCategoryGallery(@MappingTarget CategoryGallery categoryGallery, CategoryGalleryRequest request);
}

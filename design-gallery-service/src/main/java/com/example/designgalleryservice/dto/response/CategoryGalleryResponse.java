package com.example.designgalleryservice.dto.response;

import com.example.designgalleryservice.entities.CategoryGallery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGalleryResponse implements Serializable {
    private int id;
    private String name;
    private String description;
    @JsonIgnore
    private CategoryGallery parentCategoryGallery;
}

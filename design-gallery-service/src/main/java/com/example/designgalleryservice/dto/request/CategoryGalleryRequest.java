package com.example.designgalleryservice.dto.request;

import com.example.designgalleryservice.entities.CategoryGallery;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGalleryRequest implements Serializable {
    private String name;
    private String description;
    private int parentCategoryGalleryId;
    private CategoryGallery parentCategoryGallery;
}

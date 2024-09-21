package com.example.designgalleryservice.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagesDesignResponse implements Serializable {
    private Long id;
    private String name;
    private String tags;
    private String description;
    private String imageUrl;
    private int categoryGalleryId;
}

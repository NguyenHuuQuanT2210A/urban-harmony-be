package com.example.designgalleryservice.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagesDesignRequest implements Serializable {
    private String name;
    private String tags;
    private String description;
    private String imageUrl;
    private int categoryGalleryId;
}

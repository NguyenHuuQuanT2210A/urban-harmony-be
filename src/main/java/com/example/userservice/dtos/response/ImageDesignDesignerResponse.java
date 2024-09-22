package com.example.userservice.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDesignDesignerResponse {
    private Long id;
    private String imageUrl;
    private Long designerProfileId;
}

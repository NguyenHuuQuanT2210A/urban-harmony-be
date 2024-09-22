package com.example.userservice.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDesignDesignerRequest {
    private String imageUrl;
    private Long designerProfileId;
}

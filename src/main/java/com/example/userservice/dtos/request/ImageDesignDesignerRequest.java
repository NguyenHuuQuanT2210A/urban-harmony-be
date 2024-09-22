package com.example.userservice.dtos.request;

import com.example.userservice.entities.DesignerProfile;
import jakarta.persistence.*;
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

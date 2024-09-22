package com.example.userservice.dtos.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignerProfileResponse {
    private Long id;

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatar;

    private String experience;
    private String projects;
    private String skills;
    private String education;
    private String certifications;
    private Long userId;

    private String status;

    private Set<ImageDesignDesignerResponse> imagesDesignDesigner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}

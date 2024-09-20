package com.example.userservice.dtos.request;

import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignerProfileRequest{
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
    private Set<Long> imagesDesignDesignerIds;
}

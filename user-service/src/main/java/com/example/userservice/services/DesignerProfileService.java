package com.example.userservice.services;

import com.example.userservice.dtos.request.DesignerProfileRequest;
import com.example.userservice.dtos.response.DesignerProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DesignerProfileService {
    Long countDesignerProfiles();
    DesignerProfileResponse getDesignerProfileByName(String name);
    DesignerProfileResponse getDesignerProfileById(Long id);
    DesignerProfileResponse getDesignerProfileByUserId(Long userId);
    void addDesignerProfile(DesignerProfileRequest request, List<MultipartFile> imageFiles, MultipartFile avatar);
    void updateDesignerProfile(Long id, DesignerProfileRequest request, List<MultipartFile> imageFiles, MultipartFile avatar);
    DesignerProfileResponse updateStatusDesignerProfile(Long id, String status);
    void deleteDesignerProfile(Long id);
    void moveToTrash(Long id);
    Page<DesignerProfileResponse> getInTrash(Pageable pageable);
    void restoreDesignerProfile(Long id);
}

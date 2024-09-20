package com.example.userservice.services;

import com.example.userservice.dtos.request.RoomSpecificationRequest;
import com.example.userservice.dtos.response.RoomSpecificationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomSpecificationService {
    void deleteRoomSpecification(Long id);
    void deleteRoomSpecificationByUserId(Long userId);
    RoomSpecificationResponse getRoomSpecificationById(Long id);
    List<RoomSpecificationResponse> getRoomSpecificationByUserId(Long userId);
    RoomSpecificationResponse saveRoomSpecification(RoomSpecificationRequest request, List<MultipartFile> imageFiles);
    RoomSpecificationResponse updateRoomSpecification(Long id, RoomSpecificationRequest request, List<MultipartFile> imageFiles);
}

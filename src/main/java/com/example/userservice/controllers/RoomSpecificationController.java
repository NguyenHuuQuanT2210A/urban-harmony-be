package com.example.userservice.controllers;

import com.example.userservice.dtos.request.RoomSpecificationRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.RoomSpecificationResponse;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.RoomSpecificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room_specifications")
public class RoomSpecificationController {
    private final RoomSpecificationService roomSpecificationService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    ApiResponse<RoomSpecificationResponse> getRoomSpecificationById(@PathVariable Long id) {
        return ApiResponse.<RoomSpecificationResponse>builder()
                .message("Get all Room Specification By ID")
                .data(roomSpecificationService.getRoomSpecificationById(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<RoomSpecificationResponse>> getRoomSpecificationByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<RoomSpecificationResponse>>builder()
                .message("Get all Room Specification By User ID")
                .data(roomSpecificationService.getRoomSpecificationByUserId(userId))
                .build();
    }

    @DeleteMapping("/{id}")
    void deleteRoomSpecificationById(@PathVariable Long id) {
        roomSpecificationService.deleteRoomSpecification(id);
    }

    @DeleteMapping("/user/{userId}")
    void deleteRoomSpecificationByUserId(@PathVariable Long userId) {
        roomSpecificationService.deleteRoomSpecificationByUserId(userId);
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<RoomSpecificationResponse> saveProductImage(@RequestPart("roomSpecificationRequest") RoomSpecificationRequest request, @RequestParam("files") List<MultipartFile> imageFiles){
        return ApiResponse.<RoomSpecificationResponse>builder()
                .message("Create a new Room Specification")
                .data(roomSpecificationService.saveRoomSpecification(request, imageFiles))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoomSpecificationResponse> updateProductImage(@PathVariable Long id, @RequestPart("roomSpecificationRequest") RoomSpecificationRequest request,  @RequestParam("files") List<MultipartFile> imageFiles) {
        return ApiResponse.<RoomSpecificationResponse>builder()
                .message("Update Room Specification")
                .data(roomSpecificationService.updateRoomSpecification(id, request, imageFiles))
                .build();
    }

    @GetMapping("/imagesPost/{filename:.+}")
    ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadRoomSpecificationImageFileAsResource(filename);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex){
            throw new CustomException("File not found" + ex, HttpStatus.NOT_FOUND);
        }
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping ("/images/{filename:.+}")
    ResponseEntity<?> deleteFile(@PathVariable String filename){
        fileStorageService.deleteRoomSpecificationImageFile(filename);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("delete images successfully")
                .build());
    }
}

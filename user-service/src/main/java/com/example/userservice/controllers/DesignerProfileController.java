package com.example.userservice.controllers;

import com.example.userservice.dtos.request.DesignerProfileRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.DesignerProfileResponse;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.services.DesignerProfileService;
import com.example.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/designer_profile")
public class DesignerProfileController {
    private final DesignerProfileService designerProfileService;
    private final UserService userService;

    @GetMapping("/count")
    ApiResponse<Long> countDesignerProfiles() {
        return ApiResponse.<Long>builder()
                .message("Get count Designer Profiles")
                .data(designerProfileService.countDesignerProfiles())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<?> getDesignerProfileById(@PathVariable Long id) {
        DesignerProfileResponse designerProfileResponse = designerProfileService.getDesignerProfileById(id);
        if (designerProfileResponse == null) {
            throw new NotFoundException("DesignerProfile not found with id: " + id);
        }
        return ApiResponse.builder()
                .message("Get Designer Profile by Id")
                .data(designerProfileResponse)
                .build();
    }

    @GetMapping("/name/{name}")
    ApiResponse<?> getDesignerProfileByName(@PathVariable String name) {
        DesignerProfileResponse designerProfileResponse = designerProfileService.getDesignerProfileByName(name);
        if (designerProfileResponse == null) {
            throw new NotFoundException("Designer Profile not found with name: " + name);
        }
        return ApiResponse.builder()
                .message("Get Designer Profile by Name")
                .data(designerProfileResponse)
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<DesignerProfileResponse> findByUserId(@PathVariable Long userId) {
        return ApiResponse.<DesignerProfileResponse>builder()
                .message("Get Designer Profiles by User Id")
                .data(designerProfileService.getDesignerProfileByUserId(userId))
                .build();
    }

    @PostMapping(value = "/postProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addDesignerProfile(@RequestPart("designerProfileRequest") DesignerProfileRequest request, @RequestPart("files") List<MultipartFile> imageFiles, @RequestPart("avatar") MultipartFile avatar, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build());
        }
        designerProfileService.addDesignerProfile(request, imageFiles, avatar);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Create Designer Profile successfully")
                .build());
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<?> updateDesignerProfile(@PathVariable Long id, @RequestPart("designerProfileRequest") DesignerProfileRequest request, @RequestPart("files") List<MultipartFile> imageFiles, @RequestPart("avatar") MultipartFile avatar, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build());
        }
        designerProfileService.updateDesignerProfile(id, request, imageFiles, avatar);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Update Designer Profile successfully")
                .build());
    }

    @PutMapping("/updateStatus/{id}")
    ApiResponse<DesignerProfileResponse> updateStatusDesignerProfile(@PathVariable Long id, @RequestParam String status) {
        return ApiResponse.<DesignerProfileResponse>builder()
                .message("Get Designer Profiles by User Id")
                .data(designerProfileService.updateStatusDesignerProfile(id, status))
                .build();
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Long id) {
        designerProfileService.moveToTrash(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Move to trash designerProfile successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteDesignerProfile(@PathVariable Long id) {
        designerProfileService.deleteDesignerProfile(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete DesignerProfile Successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashDesignerProfile(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ApiResponse.builder()
                .message("Get in trash DesignerProfile")
                .data(designerProfileService.getInTrash(PageRequest.of(page -1, limit)))
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreDesignerProfile(@PathVariable Long id) {
        designerProfileService.restoreDesignerProfile(id);
        return ApiResponse.builder()
                .message("Restore designerProfile successfully")
                .build();
    }
}

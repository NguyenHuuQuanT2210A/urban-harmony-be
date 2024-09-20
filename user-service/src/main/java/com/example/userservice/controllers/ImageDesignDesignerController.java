package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.ImageDesignDesignerService;
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
@RequestMapping("/api/v1/image_design_designer")
public class ImageDesignDesignerController {
    private final ImageDesignDesignerService imageDesignDesignerService;
    private final FileStorageService fileStorageService;

    @GetMapping("/designerProfile/{designerProfileId}")
    ApiResponse<List<ImageDesignDesignerResponse>> getImageDesignDesignerByDesignerProfileId(@PathVariable Long designerProfileId) {
        return ApiResponse.<List<ImageDesignDesignerResponse>>builder()
                .message("Get all Product Images By Product ID")
                .data(imageDesignDesignerService.getImageDesignDesignerByDesignerProfileId(designerProfileId))
                .build();
    }

    @DeleteMapping("/{id}")
    void deleteImageDesignDesigner(@PathVariable Long id) {
        imageDesignDesignerService.deleteImageDesignDesigner(id);
    }

    @DeleteMapping("/ids")
    void deleteImageDesignDesignerByIds(@RequestParam List<Long> ids) {
        imageDesignDesignerService.deleteImageDesignDesignerByIds(ids);
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<ImageDesignDesignerResponse>> saveProductImage(@RequestParam Long designerProfileId, @RequestParam("files") List<MultipartFile> imageFiles){
        return ApiResponse.<List<ImageDesignDesignerResponse>>builder()
                .message("Create a new Product Image")
                .data(imageDesignDesignerService.saveImageDesignDesigner(designerProfileId, imageFiles))
                .build();
    }

    @PutMapping
    ApiResponse<List<ImageDesignDesignerResponse>> updateProductImage(@RequestParam Long designerProfileId, @RequestParam List<Long> productImageIds, @RequestParam("files") List<MultipartFile> imageFiles) {
        return ApiResponse.<List<ImageDesignDesignerResponse>>builder()
                .message("Update Product Image")
                .data(imageDesignDesignerService.updateImageDesignDesigner(designerProfileId, productImageIds, imageFiles))
                .build();
    }

    @GetMapping("/imagesPost/{filename:.+}")
    ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadDesignerDesignImageFileAsResource(filename);

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
        fileStorageService.deleteProductImageFile(filename);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("delete images successfully")
                .build());
    }
}

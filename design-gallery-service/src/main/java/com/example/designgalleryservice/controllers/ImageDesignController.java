package com.example.designgalleryservice.controllers;

import com.example.designgalleryservice.dto.FileUploadedDTO;
import com.example.designgalleryservice.dto.request.ImagesDesignRequest;
import com.example.designgalleryservice.dto.response.ApiResponse;
import com.example.designgalleryservice.dto.response.ImagesDesignResponse;
import com.example.designgalleryservice.exception.CustomException;
import com.example.designgalleryservice.services.FileStorageService;
import com.example.designgalleryservice.services.FileUploadService;
import com.example.designgalleryservice.services.ImagesDesignService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images_design")
public class ImageDesignController {
    private final ImagesDesignService imagesDesignService;
    private final FileStorageService fileStorageService;
    private final FileUploadService fileUploadService;

    @GetMapping("/categoryGalleryId/{categoryGalleryId}")
    ApiResponse<Page<ImagesDesignResponse>> getProductImages(@PathVariable int categoryGalleryId,
                                                             @RequestParam(defaultValue = "1", name = "page") int page,
                                                             @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<ImagesDesignResponse>>builder()
                .message("Get all Product Images By Product ID")
                .data(imagesDesignService.getImagesDesigns(categoryGalleryId, PageRequest.of(page - 1, limit)))
                .build();
    }

    @DeleteMapping("/{id}")
    void deleteProductImage(@PathVariable Long id) {
        imagesDesignService.deleteImagesDesign(id);
    }

    @DeleteMapping("/categoryGalleryId/{categoryGalleryId}")
    void deleteProductImages(@PathVariable int categoryGalleryId) {
        imagesDesignService.deleteImagesDesigns(categoryGalleryId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> saveProductImage(@RequestPart("imagesDesign")ImagesDesignRequest request, @RequestParam("file") MultipartFile imageFile, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build();
        }
        return ApiResponse.<ImagesDesignResponse>builder()
                .message("Create a new Product Image")
                .data(imagesDesignService.saveImagesDesign(request, imageFile))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<?> updateProductImage(@PathVariable Long id, @RequestPart("imagesDesign")ImagesDesignRequest request, @RequestParam("file") MultipartFile imageFile, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build();
        }
        return ApiResponse.<ImagesDesignResponse>builder()
                .message("Update Product Image")
                .data(imagesDesignService.updateImagesDesign(id, request, imageFile))
                .build();
    }

    @GetMapping("/imagesDesign/{filename:.+}")
    ResponseEntity<?> getFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadDesignImageFileAsResource(filename);

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

}

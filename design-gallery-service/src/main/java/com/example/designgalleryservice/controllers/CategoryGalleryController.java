package com.example.designgalleryservice.controllers;


import com.example.designgalleryservice.dto.request.CategoryGalleryRequest;
import com.example.designgalleryservice.dto.response.ApiResponse;
import com.example.designgalleryservice.dto.response.CategoryGalleryResponse;
import com.example.designgalleryservice.exception.CategoryNotFoundException;
import com.example.designgalleryservice.services.CategoryGalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category_gallery")
public class CategoryGalleryController {
    private final CategoryGalleryService categoryGalleryService;

    @GetMapping("/getAll")
    ApiResponse<Page<CategoryGalleryResponse>> getAllCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        Page<CategoryGalleryResponse> categoryDTOS = categoryGalleryService.getAllCategoryGallery(PageRequest.of(page - 1, limit));
        return ApiResponse.<Page<CategoryGalleryResponse>>builder()
                .message("Get all Category Gallery")
                .data(categoryDTOS)
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<CategoryGalleryResponse> getCategoryById(@PathVariable int id) {
        CategoryGalleryResponse category = categoryGalleryService.getCategoryGalleryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category Gallery not found with id: " + id);
        }
        return ApiResponse.<CategoryGalleryResponse>builder()
                .message("Get Category Gallery By Id")
                .data(category)
                .build();
    }

    @GetMapping("/categoryGalleryId/{categoryGalleryId}")
    ApiResponse<Page<CategoryGalleryResponse>> getCategoryByCategoryGalleryId(@PathVariable int categoryGalleryId,
                                                                        @RequestParam(defaultValue = "1", name = "page") int page,
                                                                        @RequestParam(defaultValue = "10", name = "limit") int limit) {
        Page<CategoryGalleryResponse> categoryDTOS = categoryGalleryService.getCategoryGalleryByCategoryGalleryParentId(categoryGalleryId, PageRequest.of(page - 1, limit));
        return ApiResponse.<Page<CategoryGalleryResponse>>builder()
                .message("Get Category Gallery By Category Gallery Id")
                .data(categoryDTOS)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<?> addCategory(@Valid @RequestBody CategoryGalleryRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("Error: " + errors.get("field"))
                    .data(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors))
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Created Category Gallery Successfully")
                .data(categoryGalleryService.addCategoryGallery(request))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryGalleryRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message("Error: " + errors.get("field"))
                    .data(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors))
                    .build());
        }
        categoryGalleryService.updateCategoryGallery(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Update Category Gallery Successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteCategory(@PathVariable int id) {
        categoryGalleryService.deleteCategoryGallery(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Category Gallery Successfully")
                .build();
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable int id) {
        categoryGalleryService.moveToTrash(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Move to Trash Category Gallery Successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashCategory(@RequestParam(defaultValue = "1", name = "page") int page, @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get In Trash Category Gallery")
                .data(categoryGalleryService.getInTrash(PageRequest.of(page - 1, limit)))
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreCategory(@PathVariable int id) {
        categoryGalleryService.restoreCategoryGallery(id);
        return ApiResponse.builder()
                .message("Restore Category Gallery Successfully")
                .build();
    }
}

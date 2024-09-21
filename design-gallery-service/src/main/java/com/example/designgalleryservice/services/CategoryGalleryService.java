package com.example.designgalleryservice.services;

import com.example.designgalleryservice.dto.request.CategoryGalleryRequest;
import com.example.designgalleryservice.dto.response.CategoryGalleryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryGalleryService {
    Page<CategoryGalleryResponse> getAllCategoryGallery(Pageable pageable);
    CategoryGalleryResponse getCategoryGalleryById(int id);
    Page<CategoryGalleryResponse> getCategoryGalleryByCategoryGalleryParentId(int id, Pageable pageable);
    CategoryGalleryResponse addCategoryGallery(CategoryGalleryRequest request);
    void updateCategoryGallery(int id, CategoryGalleryRequest request);
    void deleteCategoryGallery(int id);
    void moveToTrash(int id);
    Page<CategoryGalleryResponse> getInTrash(Pageable pageable);

    void restoreCategoryGallery(int id);

}

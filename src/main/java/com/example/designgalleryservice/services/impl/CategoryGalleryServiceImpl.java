package com.example.designgalleryservice.services.impl;

import com.example.designgalleryservice.dto.request.CategoryGalleryRequest;
import com.example.designgalleryservice.dto.response.CategoryGalleryResponse;
import com.example.designgalleryservice.entities.CategoryGallery;
import com.example.designgalleryservice.mapper.CategoryGalleryMapper;
import com.example.designgalleryservice.repositories.CategoryGalleryRepository;
import com.example.designgalleryservice.services.CategoryGalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryGalleryServiceImpl implements CategoryGalleryService {
    private final CategoryGalleryRepository categoryGalleryRepository;
    private final CategoryGalleryMapper categoryGalleryMapper;

    @Override
    public Page<CategoryGalleryResponse> getAllCategoryGallery(Pageable pageable) {
        return categoryGalleryRepository.findAll(pageable).map(categoryGalleryMapper::toCategoryGalleryResponse);
    }

    @Override
    public CategoryGalleryResponse getCategoryGalleryById(int id) {
        return categoryGalleryMapper.toCategoryGalleryResponse(findCategoryGalleryById(id));
    }

    @Override
    public Page<CategoryGalleryResponse> getCategoryGalleryByCategoryGalleryParentId(int id, Pageable pageable) {
        findCategoryGalleryById(id);
        return categoryGalleryRepository.findByParentCategoryGalleryId(id, pageable).map(categoryGalleryMapper::toCategoryGalleryResponse);
    }

    @Override
    public CategoryGalleryResponse addCategoryGallery(CategoryGalleryRequest request) {
        if (request.getParentCategoryGalleryId() != 0) {
            request.setParentCategoryGallery(findCategoryGalleryById(request.getParentCategoryGalleryId()));
        }
        return categoryGalleryMapper.toCategoryGalleryResponse(categoryGalleryRepository.save(categoryGalleryMapper.categoryGalleryRequesttoCategoryGallery(request)));
    }

    @Override
    public void updateCategoryGallery(int id, CategoryGalleryRequest request) {
        CategoryGallery categoryGallery = findCategoryGalleryById(id);
        if (request.getParentCategoryGalleryId() != 0 && request.getParentCategoryGalleryId() != categoryGallery.getId()) {
            request.setParentCategoryGallery(findCategoryGalleryById(request.getParentCategoryGalleryId()));
        }
        categoryGalleryMapper.updatedCategoryGallery(categoryGallery, request);
        categoryGalleryRepository.save(categoryGallery);
    }

    @Override
    public void deleteCategoryGallery(int id) {
        categoryGalleryRepository.deleteById(id);
    }

    @Override
    public void moveToTrash(int id) {
        CategoryGallery categoryGallery = findCategoryGalleryById(id);
        categoryGallery.setDeletedAt(LocalDateTime.now());
        categoryGalleryRepository.save(categoryGallery);
    }

    @Override
    public Page<CategoryGalleryResponse> getInTrash(Pageable pageable) {
        return categoryGalleryRepository.findByDeletedAtIsNotNull(pageable).map(categoryGalleryMapper::toCategoryGalleryResponse);
    }

    @Override
    public void restoreCategoryGallery(int id) {
        CategoryGallery categoryGallery = findCategoryGalleryById(id);
        categoryGallery.setDeletedAt(null);
        categoryGalleryRepository.save(categoryGallery);
    }

    private CategoryGallery findCategoryGalleryById(int id) {
        return categoryGalleryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Gallery not found"));
    }
}
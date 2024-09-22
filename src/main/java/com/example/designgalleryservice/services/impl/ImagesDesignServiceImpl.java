package com.example.designgalleryservice.services.impl;

import com.example.designgalleryservice.dto.request.ImagesDesignRequest;
import com.example.designgalleryservice.dto.response.CategoryGalleryResponse;
import com.example.designgalleryservice.dto.response.ImagesDesignResponse;
import com.example.designgalleryservice.entities.ImagesDesign;
import com.example.designgalleryservice.exception.CustomException;
import com.example.designgalleryservice.mapper.CategoryGalleryMapper;
import com.example.designgalleryservice.mapper.ImagesDesignMapper;
import com.example.designgalleryservice.repositories.ImagesDesignRepository;
import com.example.designgalleryservice.services.CategoryGalleryService;
import com.example.designgalleryservice.services.FileStorageService;
import com.example.designgalleryservice.services.ImagesDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImagesDesignServiceImpl implements ImagesDesignService {
    private final ImagesDesignRepository imagesDesignRepository;
    private final ImagesDesignMapper imagesDesignMapper;
    private final CategoryGalleryMapper categoryGalleryMapper;
    private final CategoryGalleryService categoryGalleryService;
    private final FileStorageService fileStorageService;

    @Override
    public void deleteImagesDesign(Long id) {
        var imageDesign = imagesDesignRepository.findById(id);
        if (imageDesign.isEmpty()) {
            throw new CustomException("Image design not found with id: " + id, HttpStatus.BAD_REQUEST);
        }
        fileStorageService.deleteDesignImageFile(imageDesign.get().getImageUrl());
        imagesDesignRepository.deleteById(id);
    }

    @Override
    public void deleteImagesDesigns(int categoryGalleryId) {
        getCategoryGalleryById(categoryGalleryId);
        var imagesDesigns = imagesDesignRepository.findByCategoryGalleryId(categoryGalleryId);
        for (ImagesDesign imagesDesign : imagesDesigns) {
            fileStorageService.deleteDesignImageFile(imagesDesign.getImageUrl());
            imagesDesignRepository.deleteById(imagesDesign.getId());
        }
    }

    @Override
    public Page<ImagesDesignResponse> getImagesDesignsByCategoryGalleryId(int categoryGalleryId, Pageable pageable) {
        return imagesDesignRepository.findByCategoryGalleryId(categoryGalleryId, pageable).map(imagesDesignMapper::toImagesDesignResponse);
    }

    @Override
    public ImagesDesignResponse saveImagesDesign(ImagesDesignRequest request, MultipartFile imageFile) {
        var categoryGallery = getCategoryGalleryById(request.getCategoryGalleryId());
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        if (categoryGallery == null) {
            throw new CustomException("Category Gallery not found with id: " + request.getCategoryGalleryId(), HttpStatus.BAD_REQUEST);
        }

        ImagesDesign imagesDesign = imagesDesignMapper.toImagesDesign(request);
        imagesDesign.setImageUrl(fileStorageService.storeDesignImageFile(imageFile));
        imagesDesign.setCategoryGallery(categoryGalleryMapper.categoryGalleryResponsetoCategoryGallery(categoryGallery));

        return imagesDesignMapper.toImagesDesignResponse(imagesDesignRepository.save(imagesDesign));
    }

    @Override
    public ImagesDesignResponse updateImagesDesign(Long id, ImagesDesignRequest request, MultipartFile imageFile) {
        var categoryGallery = getCategoryGalleryById(request.getCategoryGalleryId());
        if (imageFile == null) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        if (categoryGallery == null) {
            throw new CustomException("Category Gallery not found with id: " + request.getCategoryGalleryId(), HttpStatus.BAD_REQUEST);
        }

        var imagesDesign = imagesDesignRepository.findById(id).orElseThrow(() -> new CustomException("Image design not found with id: " + id, HttpStatus.BAD_REQUEST));

        if (!Objects.equals(imageFile.getOriginalFilename(), "")){
            fileStorageService.deleteDesignImageFile(imagesDesign.getImageUrl());
            imagesDesign.setImageUrl(fileStorageService.storeDesignImageFile(imageFile));
            imagesDesign.setCategoryGallery(categoryGalleryMapper.categoryGalleryResponsetoCategoryGallery(categoryGallery));
        }

        return imagesDesignMapper.toImagesDesignResponse(imagesDesignRepository.save(imagesDesign));
    }

    @Override
    public Page<ImagesDesignResponse> getImagesDesigns(Pageable pageable) {
        return imagesDesignRepository.findAll(pageable).map(imagesDesignMapper::toImagesDesignResponse);
    }

    @Override
    public ImagesDesignResponse getImagesDesignById(Long id) {
        return imagesDesignRepository.findById(id).map(imagesDesignMapper::toImagesDesignResponse).orElseThrow(() -> new CustomException("Image design not found with id: " + id, HttpStatus.BAD_REQUEST));
    }

    private CategoryGalleryResponse getCategoryGalleryById(int categoryGalleryId) {
        return categoryGalleryService.getCategoryGalleryById(categoryGalleryId);
    }
}

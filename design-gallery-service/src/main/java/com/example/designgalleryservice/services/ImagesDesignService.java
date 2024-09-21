package com.example.designgalleryservice.services;

import com.example.designgalleryservice.dto.request.ImagesDesignRequest;
import com.example.designgalleryservice.dto.response.ImagesDesignResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImagesDesignService {
    void deleteImagesDesign(Long id);
    void deleteImagesDesigns(int categoryGalleryId);
    Page<ImagesDesignResponse> getImagesDesignsByCategoryGalleryId(int categoryGalleryId, Pageable pageable);
    ImagesDesignResponse saveImagesDesign(ImagesDesignRequest request, MultipartFile imageFile);
    ImagesDesignResponse updateImagesDesign(Long id, ImagesDesignRequest request, MultipartFile imageFile);
    Page<ImagesDesignResponse> getImagesDesigns(Pageable pageable);
}

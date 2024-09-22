package com.example.userservice.services;

import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageDesignDesignerService {
    void deleteImageDesignDesigner(Long id);
    void deleteImageDesignDesignerByDesignerProfileId(Long designerProfileId);
    List<ImageDesignDesignerResponse> getImageDesignDesignerByDesignerProfileId(Long designerProfileId);
    List<ImageDesignDesignerResponse> saveImageDesignDesigner(Long designerProfileId, List<MultipartFile> imageFiles);
    List<ImageDesignDesignerResponse> updateImageDesignDesigner(Long designerProfileId, List<Long> imageDesignDesignerIds, List<MultipartFile> imageFiles);
    void deleteImageDesignDesignerByIds(List<Long> ids);
}

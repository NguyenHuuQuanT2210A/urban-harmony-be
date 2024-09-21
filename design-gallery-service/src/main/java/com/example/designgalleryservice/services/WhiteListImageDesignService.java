package com.example.designgalleryservice.services;


import com.example.designgalleryservice.entities.WhiteListImageDesign;
import com.example.designgalleryservice.entities.WhiteListImageDesignId;

import java.util.List;
import java.util.Optional;

public interface WhiteListImageDesignService {
    List<WhiteListImageDesign> getAllWhiteListImageDesign();
    Optional<WhiteListImageDesign> getWhiteListImageDesignById(WhiteListImageDesignId id);
    List<WhiteListImageDesign> getWhiteListImageDesignByUserId(Long userId);
    Long getCountWhiteListImageDesign(Long userId);
    Long getWhiteListImageDesignByImageDesignId(Long imagesDesignId);
    String addWhiteListImageDesign(WhiteListImageDesignId id);
    void deleteWhiteListImageDesign(WhiteListImageDesignId id);
}

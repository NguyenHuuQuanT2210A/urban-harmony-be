package com.example.designgalleryservice.repositories;

import com.example.designgalleryservice.dto.response.ImagesDesignResponse;
import com.example.designgalleryservice.entities.ImagesDesign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesDesignRepository extends JpaRepository<ImagesDesign, Long> {
    Page<ImagesDesign> findByCategoryGalleryId(int categoryGalleryId, Pageable pageable);
    List<ImagesDesign> findByCategoryGalleryId(int categoryGalleryId);
}
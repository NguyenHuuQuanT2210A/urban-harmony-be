package com.example.designgalleryservice.repositories;

import com.example.designgalleryservice.entities.CategoryGallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryGalleryRepository extends JpaRepository<CategoryGallery, Integer> {
    Page<CategoryGallery> findByDeletedAtIsNotNull(Pageable pageable);
    Page<CategoryGallery> findByParentCategoryGalleryId(int parentCategoryGalleryId, Pageable pageable);
}
package com.example.designgalleryservice.repositories;


import com.example.designgalleryservice.entities.WhiteListImageDesign;
import com.example.designgalleryservice.entities.WhiteListImageDesignId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteListImageDesignRepository extends JpaRepository<WhiteListImageDesign, WhiteListImageDesignId> {
    @Query("SELECT w FROM WhiteListImageDesign w WHERE w.id.userId = :userId")
    List<WhiteListImageDesign> findAllByUserId(Long userId);

    List<WhiteListImageDesign> findAllByImagesDesignId(@Param("imagesDesignId") Long imagesDesignId);
}

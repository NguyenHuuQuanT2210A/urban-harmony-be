package com.example.userservice.repositories;

import com.example.userservice.entities.ImageDesignDesigner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageDesignDesignerRepository extends JpaRepository<ImageDesignDesigner, Long> {
    List<ImageDesignDesigner> findByDesignerProfileId(Long designerProfileId);
    boolean existsById(Long id);

    @Modifying
    @Query("DELETE FROM ImageDesignDesigner idd WHERE idd.id = :id")
    void deleteById(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM ImageDesignDesigner idd WHERE idd.designerProfile.id = :designerProfileId")
    void deleteAllByDesignerProfileId(@Param("designerProfileId") Long designerProfileId);
}

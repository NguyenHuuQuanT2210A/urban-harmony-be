package com.example.userservice.repositories;

import com.example.userservice.entities.DesignerProfile;
import com.example.userservice.statics.enums.DesignerProfileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignerProfileRepository extends JpaRepository<DesignerProfile, Long>, JpaSpecificationExecutor<DesignerProfile> {
    DesignerProfile findByUsernameAndDeletedAtIsNull(String username);
    Page<DesignerProfile> findAll(Pageable pageable);
    Page<DesignerProfile> findByDeletedAtIsNotNull(Pageable pageable);
    boolean existsByUsername(String username);
    DesignerProfile findByUserIdAndDeletedAtIsNull(Long userId);
    List<DesignerProfile> findByStatus(DesignerProfileStatus status);
}

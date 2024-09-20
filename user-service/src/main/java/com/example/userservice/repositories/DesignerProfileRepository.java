package com.example.userservice.repositories;

import com.example.userservice.entities.DesignerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignerProfileRepository extends JpaRepository<DesignerProfile, Long>, JpaSpecificationExecutor<DesignerProfile> {
    DesignerProfile findByUsernameAndDeletedAtIsNull(String username);
    Page<DesignerProfile> findAll(Pageable pageable);
    Page<DesignerProfile> findByDeletedAtIsNotNull(Pageable pageable);
    boolean existsByUsername(String username);
    DesignerProfile findByUserIdAndDeletedAtIsNull(Long userId);
}

package com.example.userservice.repositories;

import com.example.userservice.entities.RoomSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomSpecificationRepository extends JpaRepository<RoomSpecification, Long> {
    @Modifying
    void deleteByUserId(Long userId);
    List<RoomSpecification> findByUserId(Long userId);
}

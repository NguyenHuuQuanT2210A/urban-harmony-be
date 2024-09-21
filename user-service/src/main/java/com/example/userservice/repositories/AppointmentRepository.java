package com.example.userservice.repositories;

import com.example.userservice.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findAllByUserId(Long userId, Pageable pageable);
    Page<Appointment> findAllByDesignerId(Long designerId, Pageable pageable);
    List<Appointment> findAllByDatetimeStartBetweenAndDesignerId(LocalDateTime start, LocalDateTime end, Long designerId);
}

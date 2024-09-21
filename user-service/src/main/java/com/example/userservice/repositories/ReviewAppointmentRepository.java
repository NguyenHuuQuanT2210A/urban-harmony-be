package com.example.userservice.repositories;

import com.example.userservice.entities.ReviewAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewAppointmentRepository extends JpaRepository<ReviewAppointment, Long> {
    ReviewAppointment findByAppointmentId(Long appointmentId);
    List<ReviewAppointment> findByUserId(Long userId);
}

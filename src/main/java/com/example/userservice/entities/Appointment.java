package com.example.userservice.entities;

import com.example.userservice.entities.base.BaseEntity;
import com.example.userservice.statics.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @Column(name = "appointment_url")
    private String appointmentUrl;

    @ManyToOne
    @JoinColumn(name = "designer_id", referencedColumnName = "id", nullable = false)
    private User designer;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

package com.example.userservice.entities;

import com.example.userservice.entities.base.BaseEntity;
import com.example.userservice.statics.enums.DesignerProfileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "designer_profile")
public class DesignerProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatar;

    private String skills;
    private String experience;
    private String projects;
    private String education;
    private String certifications;

    @Enumerated(EnumType.STRING)
    private DesignerProfileStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "designerProfile", fetch = FetchType.LAZY)
    private Set<ImageDesignDesigner> imageDesignDesigner;
}

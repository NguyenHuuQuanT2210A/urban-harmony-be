package com.example.userservice.entities;

import com.example.userservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image_design_designer")
public class ImageDesignDesigner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "designer_profile_id", referencedColumnName = "id")
    private DesignerProfile designerProfile;
}

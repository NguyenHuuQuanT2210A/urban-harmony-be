package com.example.designgalleryservice.entities;

import com.example.designgalleryservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories_gallery")
public class CategoryGallery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_categoryGallery_id")
    private CategoryGallery parentCategoryGallery;
}

package com.example.designgalleryservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "white_list_image_design")
public class WhiteListImageDesign {
    @EmbeddedId
    private WhiteListImageDesignId id = new WhiteListImageDesignId();
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "images_design_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private ImagesDesign imagesDesign;
}

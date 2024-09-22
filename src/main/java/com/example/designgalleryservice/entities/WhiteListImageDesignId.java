package com.example.designgalleryservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
//Serializable => serialize + deserialize file io
public class WhiteListImageDesignId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "images_design_id")
    private Long imagesDesignId;
}

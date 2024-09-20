package com.example.userservice.dtos.request;

import com.example.userservice.entities.Appointment;
import com.example.userservice.entities.User;
import com.example.userservice.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomSpecificationRequest{
    private String room_length;
    private String room_width;
    private String color;
    private List<String> imageList;
    private Long userId;
    private Long appointmentId;
}

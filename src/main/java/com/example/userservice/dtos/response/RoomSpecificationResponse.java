package com.example.userservice.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomSpecificationResponse {
    private Long id;
    private String room_length;
    private String room_width;
    private String color;
    private String phone;
    private List<String> imageList;
    private Long userId;
    private Long appointmentId;
}

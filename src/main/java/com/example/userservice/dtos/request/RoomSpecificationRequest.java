package com.example.userservice.dtos.request;

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
    private String phone;
    private List<String> imageList;
    private Long userId;
    private Long appointmentId;
}

package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.RoomSpecificationRequest;
import com.example.userservice.dtos.response.DesignerProfileResponse;
import com.example.userservice.dtos.response.RoomSpecificationResponse;
import com.example.userservice.entities.DesignerProfile;
import com.example.userservice.entities.RoomSpecification;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.mappers.AppointmentMapper;
import com.example.userservice.mappers.DesignerProfileMapper;
import com.example.userservice.mappers.RoomSpecificationMapper;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.DesignerProfileRepository;
import com.example.userservice.repositories.RoomSpecificationRepository;
import com.example.userservice.services.*;
import com.example.userservice.services.RoomSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomSpecificationServiceImpl implements RoomSpecificationService {
    private final RoomSpecificationRepository roomSpecificationRepository;
    private final RoomSpecificationMapper roomSpecificationMapper;
    private final FileStorageService fileStorageService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @Override
    public void deleteRoomSpecification(Long id) {
        var roomSpecification = roomSpecificationRepository.findById(id);
        if (roomSpecification.isEmpty()) {
            throw new CustomException("Room Specification not found with id: " + id, HttpStatus.BAD_REQUEST);
        }
        String[] currentImageUrls = roomSpecification.get().getImageList().split(",");
        for (String imageUrl : currentImageUrls) {
            fileStorageService.deleteRoomSpecificationImageFile(imageUrl);
        }
        roomSpecificationRepository.deleteById(id);
    }

    @Override
    public void deleteRoomSpecificationByUserId(Long userId) {
        List<RoomSpecification> roomSpecifications = roomSpecificationRepository.findByUserId(userId);
        for (RoomSpecification roomSpecification : roomSpecifications) {
            String[] currentImageUrls = roomSpecification.getImageList().split(",");
            for (String imageUrl : currentImageUrls) {
                fileStorageService.deleteRoomSpecificationImageFile(imageUrl);
            }
            roomSpecificationRepository.deleteById(roomSpecification.getId());
        }
    }

    @Override
    public RoomSpecificationResponse getRoomSpecificationById(Long id) {
        RoomSpecification roomSpecification = findByRoomSpecificationId(id);

        var roomSpecificationResponse = roomSpecificationMapper.toRoomSpecificationResponse(roomSpecificationRepository.save(roomSpecification));
        roomSpecificationResponse.setImageList(Arrays.asList(roomSpecification.getImageList().split(",")));
        return roomSpecificationResponse;
    }

    @Override
    public List<RoomSpecificationResponse> getRoomSpecificationByUserId(Long userId) {
        List<RoomSpecification> roomSpecifications = roomSpecificationRepository.findByUserId(userId);

        return roomSpecifications.stream()
                .sorted(Comparator.comparing(RoomSpecification::getCreatedAt)) // Sắp xếp theo createdAt
                .map(roomSpecification -> {
                    var roomSpecificationResponse = roomSpecificationMapper.toRoomSpecificationResponse(roomSpecification);
                    roomSpecificationResponse.setImageList(Arrays.asList(roomSpecification.getImageList().split(",")));
                    return roomSpecificationResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomSpecificationResponse saveRoomSpecification(RoomSpecificationRequest request, List<MultipartFile> imageFiles) {
        var user = userService.findById(request.getUserId());
        var appointment = appointmentService.getAppointmentById(request.getAppointmentId());
        if (user == null) {
            throw new NotFoundException("User not found with id: " + request.getUserId());
        }
        if (appointment == null) {
            throw new NotFoundException("Appointment not found with id: " + request.getAppointmentId());
        }

        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        StringBuilder imageUrlBuilder = new StringBuilder();
        for (MultipartFile imageFile : imageFiles) {
            String imageUrl = fileStorageService.storeDesignerDesignImageFile(imageFile);

            if (imageUrlBuilder.length() > 0) {
                imageUrlBuilder.append(",");
            }
            imageUrlBuilder.append(imageUrl);
        }

        RoomSpecification roomSpecification = roomSpecificationMapper.toRoomSpecification(request);
        roomSpecification.setImageList(imageUrlBuilder.toString());
        roomSpecification.setUser(userMapper.userDTOToUser(user));
        roomSpecification.setAppointment(appointmentMapper.appointmentResponseToAppointment(appointment));
        var roomSpecificationResponse = roomSpecificationMapper.toRoomSpecificationResponse(roomSpecificationRepository.save(roomSpecification));
        roomSpecificationResponse.setImageList(Arrays.asList(roomSpecification.getImageList().split(",")));
        return roomSpecificationResponse;
    }

    @Override
    @Transactional
    public RoomSpecificationResponse updateRoomSpecification(Long id, RoomSpecificationRequest request, List<MultipartFile> imageFiles) {
        // Tìm roomSpecification theo ID
        var updatedRoomSpecification = findByRoomSpecificationId(id);
        roomSpecificationMapper.updateRoomSpecification(updatedRoomSpecification, request);

        // Kiểm tra và cập nhật User và Appointment nếu có
        if (request.getUserId() != null || request.getAppointmentId() != null) {
            updatedRoomSpecification.setUser(updatedRoomSpecification.getUser());
            updatedRoomSpecification.setAppointment(updatedRoomSpecification.getAppointment());
        }

        String imageList = updatedRoomSpecification.getImageList() != null ? updatedRoomSpecification.getImageList() : "";
        List<String> currentImageUrls = imageList.isEmpty() ? new ArrayList<>() : Arrays.asList(imageList.split(","));
        List<String> imageUrlList = request.getImageList() != null ? request.getImageList() : new ArrayList<>();

        List<String> remainingImages = currentImageUrls.stream()
                .filter(imageUrl -> !imageUrlList.contains(imageUrl))
                .collect(Collectors.toList());

        for (String imageUrl : remainingImages) {
            fileStorageService.deleteRoomSpecificationImageFile(imageUrl);
        }

        List<String> updatedImageUrls = currentImageUrls.stream()
                .filter(imageUrlList::contains)
                .collect(Collectors.toList());

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String newImageUrl = fileStorageService.storeRoomSpecificationImageFile(imageFile);
                    updatedImageUrls.add(newImageUrl);  // Thêm ảnh mới vào danh sách
                }
            }
        }

        String updatedImageList = String.join(",", updatedImageUrls);
        updatedRoomSpecification.setImageList(updatedImageList.isEmpty() ? null : updatedImageList);
        roomSpecificationRepository.save(updatedRoomSpecification);

        var updatedRoomSpecificationResponse = roomSpecificationMapper.toRoomSpecificationResponse(updatedRoomSpecification);
        updatedRoomSpecificationResponse.setImageList(updatedImageUrls);
        return updatedRoomSpecificationResponse;
    }

    private RoomSpecification findByRoomSpecificationId(Long id) {
        return roomSpecificationRepository.findById(id).orElseThrow(() -> new CustomException("Room Specification not found with id: " + id, HttpStatus.BAD_REQUEST));
    }
}

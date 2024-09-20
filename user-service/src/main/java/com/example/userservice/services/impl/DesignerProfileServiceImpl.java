package com.example.userservice.services.impl;

import com.example.userservice.dtos.UserDTO;
import com.example.userservice.dtos.request.DesignerProfileRequest;
import com.example.userservice.dtos.response.DesignerProfileResponse;
import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import com.example.userservice.entities.DesignerProfile;
import com.example.userservice.entities.User;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.DesignerProfileMapper;
import com.example.userservice.mappers.UserMapper;
import com.example.userservice.repositories.DesignerProfileRepository;
import com.example.userservice.services.DesignerProfileService;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.ImageDesignDesignerService;
import com.example.userservice.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class DesignerProfileServiceImpl implements DesignerProfileService {
    private final DesignerProfileRepository designerProfileRepository;
    private final DesignerProfileMapper designerProfileMapper;
    private final ImageDesignDesignerService imageDesignDesignerService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;

    @Override
    public Long countDesignerProfiles() {
        return designerProfileRepository.count();
    }

    @Override
    public DesignerProfileResponse getDesignerProfileByName(String name) {
        DesignerProfile designerProfile = designerProfileRepository.findByUsernameAndDeletedAtIsNull(name);
        if (designerProfile == null) {
            throw new CustomException("DesignerProfile not found with name: " + name, HttpStatus.BAD_REQUEST);
        }
        return designerProfileMapper.INSTANCE.toDesignerProfileResponse(designerProfile);
    }

    @Override
    public DesignerProfileResponse getDesignerProfileById(Long id) {
        DesignerProfile designerProfile = findDesignerProfileById(id);
        var designerProfileResponse = designerProfileMapper.INSTANCE.toDesignerProfileResponse(designerProfile);

        return designerProfileResponse;
    }

    @Override
    public DesignerProfileResponse getDesignerProfileByUserId(Long userId) {
        return designerProfileMapper.INSTANCE.toDesignerProfileResponse(designerProfileRepository.findByUserIdAndDeletedAtIsNull(userId));
    }


    @Override
    public void addDesignerProfile(DesignerProfileRequest request, List<MultipartFile> imageFiles, MultipartFile avatar) {
        if (designerProfileRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("DesignerProfile already exists with name: " + request.getUsername(), HttpStatus.CONFLICT);
        }

        UserDTO userDTO = userService.findById(request.getUserId());

        if (userDTO == null) {
            throw new CustomException("Can not find user with id " + request.getUserId(), HttpStatus.NOT_FOUND);
        }

        if (Objects.equals(imageFiles.get(0).getOriginalFilename(), "")){
            throw new CustomException("Images is required", HttpStatus.BAD_REQUEST);
        }

        DesignerProfile designerProfile = designerProfileMapper.INSTANCE.toDesignerProfile(request);
        designerProfile.setAvatar(fileStorageService.storeUserImageFile(avatar));
        designerProfile.setUser(userMapper.INSTANCE.userDTOToUser(userDTO));

        designerProfileRepository.save(designerProfile);

        imageDesignDesignerService.saveImageDesignDesigner(designerProfile.getId(), imageFiles);
    }

    @Override
    public void updateDesignerProfile(Long id, DesignerProfileRequest request, List<MultipartFile> imageFiles, MultipartFile avatar) {
        DesignerProfile existingDesignerProfile = findDesignerProfileById(id);
        if (request.getUsername() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getPhoneNumber() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getAddress() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getSkills() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getExperience() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getProjects() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getEducation() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (request.getCertifications() != null) {
            existingDesignerProfile.setUsername(request.getUsername());
        }

        if (!Objects.equals(avatar.getOriginalFilename(), "") || avatar != null) {
            fileStorageService.deleteUserImageFile(existingDesignerProfile.getAvatar());
            existingDesignerProfile.setAvatar(fileStorageService.storeUserImageFile(avatar));
        }

        if (request.getUserId() != null) {
            UserDTO userDTO = userService.findById(request.getUserId());
            if (userDTO == null) {
                throw new CustomException("Can not find user with id " + request.getUserId(), HttpStatus.NOT_FOUND);
            }

            User user = userMapper.INSTANCE.userDTOToUser(userDTO);
            existingDesignerProfile.setUser(user);
        }

        designerProfileRepository.save(existingDesignerProfile);

        List<Long> designerProfileImageIds = new ArrayList<>();
        List<ImageDesignDesignerResponse> imageDesignDesignerResponseList = imageDesignDesignerService.getImageDesignDesignerByDesignerProfileId(existingDesignerProfile.getId());
        for (ImageDesignDesignerResponse imageDesignDesignerResponse : imageDesignDesignerResponseList) {
            if (!request.getImagesDesignDesignerIds().contains(imageDesignDesignerResponse.getId())) {
                designerProfileImageIds.add(imageDesignDesignerResponse.getId());
            }
        }
        imageDesignDesignerService.updateImageDesignDesigner(existingDesignerProfile.getId(), designerProfileImageIds , imageFiles);
    }


    @Override
    public void deleteDesignerProfile(Long id) {
        findDesignerProfileById(id);
        imageDesignDesignerService.deleteImageDesignDesignerByDesignerProfileId(id);
        designerProfileRepository.deleteById(id);
    }

    @Override
    public void moveToTrash(Long id) {
        DesignerProfile designerProfile = findDesignerProfileById(id);
        LocalDateTime now = LocalDateTime.now();
        designerProfile.setDeletedAt(now);
        designerProfileRepository.save(designerProfile);
    }

    @Override
    public Page<DesignerProfileResponse> getInTrash(Pageable pageable) {
        Page<DesignerProfile> designerProfiles = designerProfileRepository.findByDeletedAtIsNotNull(pageable);
        return designerProfiles.map(designerProfileMapper.INSTANCE::toDesignerProfileResponse);
    }

    private DesignerProfile findDesignerProfileById(long id) {
        return designerProfileRepository.findById(id).orElseThrow(() -> new CustomException("DesignerProfile not found with id: " + id, HttpStatus.BAD_REQUEST));
    }

    @Override
    public void restoreDesignerProfile(Long id) {
        DesignerProfile designerProfile = findDesignerProfileById(id);
        designerProfile.setDeletedAt(null);
        designerProfileRepository.save(designerProfile);
    }
}
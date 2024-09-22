package com.example.userservice.services.impl;

import com.example.userservice.dtos.response.DesignerProfileResponse;
import com.example.userservice.dtos.response.ImageDesignDesignerResponse;
import com.example.userservice.entities.DesignerProfile;
import com.example.userservice.entities.ImageDesignDesigner;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.mappers.DesignerProfileMapper;
import com.example.userservice.mappers.ImageDesignDesignerMapper;
import com.example.userservice.repositories.DesignerProfileRepository;
import com.example.userservice.repositories.ImageDesignDesignerRepository;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.ImageDesignDesignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageDesignDesignerServiceImpl implements ImageDesignDesignerService {
    private final ImageDesignDesignerRepository imageDesignDesignerRepository;
    private final ImageDesignDesignerMapper imageDesignDesignerMapper;
    private final DesignerProfileRepository designerProfileRepository;
    private final DesignerProfileMapper designerProfileMapper;
    private final FileStorageService fileStorageService;

    @Override
    public void deleteImageDesignDesigner(Long id) {
        var imageDesignDesigner = imageDesignDesignerRepository.findById(id);
        if (imageDesignDesigner.isEmpty()) {
            throw new CustomException("Image Design Designer image not found with id: " + id, HttpStatus.BAD_REQUEST);
        }
        fileStorageService.deleteDesignerDesignImageFile(imageDesignDesigner.get().getImageUrl());
        imageDesignDesignerRepository.deleteById(id);
    }

    @Override
    public void deleteImageDesignDesignerByDesignerProfileId(Long designerProfileId) {
        List<ImageDesignDesigner> imageDesignDesigners = imageDesignDesignerRepository.findByDesignerProfileId(designerProfileId);
        for (ImageDesignDesigner imageDesignDesigner : imageDesignDesigners) {
            fileStorageService.deleteDesignerDesignImageFile(imageDesignDesigner.getImageUrl());
            imageDesignDesignerRepository.deleteById(imageDesignDesigner.getId());
        }
    }

    @Override
    public List<ImageDesignDesignerResponse> getImageDesignDesignerByDesignerProfileId(Long designerProfileId) {
        List<ImageDesignDesigner> imageDesignDesigners = imageDesignDesignerRepository.findByDesignerProfileId(designerProfileId);

        return imageDesignDesigners.stream()
                .sorted(Comparator.comparing(ImageDesignDesigner::getCreatedAt)) // Sắp xếp theo createdAt
                .map(imageDesignDesignerMapper.INSTANCE::toImageDesignDesignerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ImageDesignDesignerResponse> saveImageDesignDesigner(Long designerProfileId, List<MultipartFile> imageFiles) {
        DesignerProfile designerProfile = findDesignerProfileById(designerProfileId);
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        if (designerProfile == null) {
            throw new CustomException("Designer Profile not found with id: " + designerProfileId, HttpStatus.BAD_REQUEST);
        }

        List<ImageDesignDesignerResponse> imageDesignDesignerResponseList = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            ImageDesignDesigner imageDesignDesigner = new ImageDesignDesigner();
            imageDesignDesigner.setImageUrl(fileStorageService.storeDesignerDesignImageFile(imageFile));
            imageDesignDesigner.setDesignerProfile(designerProfile);
            imageDesignDesignerRepository.save(imageDesignDesigner);
            imageDesignDesignerResponseList.add(imageDesignDesignerMapper.INSTANCE.toImageDesignDesignerResponse(imageDesignDesigner));
        }
        return imageDesignDesignerResponseList;
    }

    @Override
    @Transactional
    public List<ImageDesignDesignerResponse> updateImageDesignDesigner(Long designerProfileId, List<Long> imageDesignDesignerIds, List<MultipartFile> imageFiles) {
        DesignerProfile designerProfile = findDesignerProfileById(designerProfileId);

        if (designerProfile == null) {
            throw new CustomException("Designer Profile not found with id: " + designerProfileId, HttpStatus.BAD_REQUEST);
        }

        // Delete old Image Design Designer
        for (Long imageDesignDesignerId : imageDesignDesignerIds) {
            if (imageDesignDesignerRepository.findById(imageDesignDesignerId).isEmpty()) {
                throw new CustomException("Image Design Designer not found with id: " + imageDesignDesignerId, HttpStatus.BAD_REQUEST);
            }
            if (!imageDesignDesignerRepository.findById(imageDesignDesignerId).get().getDesignerProfile().getId().equals(designerProfileId)) {
                throw new CustomException("Image Design Designer not found with id: " + imageDesignDesignerId + " for designer profile id: " + designerProfileId, HttpStatus.BAD_REQUEST);
            }
            imageDesignDesignerRepository.deleteById(imageDesignDesignerId);
        }

        // Add new Images Design Designer
        var imageDesignDesignerResponseList = imageDesignDesignerRepository.findByDesignerProfileId(designerProfile.getId()).stream().map(imageDesignDesignerMapper::toImageDesignDesignerResponse).toList();
        List<ImageDesignDesignerResponse> updatedImageDesignDesigner = new ArrayList<>(imageDesignDesignerResponseList);

        if (imageFiles.size() == 1 && Objects.equals(imageFiles.get(0).getOriginalFilename(), "")){
            return updatedImageDesignDesigner;
        }

        for (MultipartFile imageFile : imageFiles) {
            ImageDesignDesigner imageDesignDesigner = new ImageDesignDesigner();
            imageDesignDesigner.setDesignerProfile(designerProfile);
            imageDesignDesigner.setImageUrl(fileStorageService.storeDesignerDesignImageFile(imageFile));
            ImageDesignDesigner updatedProductImage = imageDesignDesignerRepository.save(imageDesignDesigner);
            updatedImageDesignDesigner.add(imageDesignDesignerMapper.INSTANCE.toImageDesignDesignerResponse(updatedProductImage));
        }
        return updatedImageDesignDesigner;
    }

    @Override
    public void deleteImageDesignDesignerByIds(List<Long> ids) {
        for (Long id : ids) {
            deleteImageDesignDesigner(id);
        }
    }

    private DesignerProfileResponse getDesignerProfileById(Long designerProfileId) {
        DesignerProfile designerProfile = designerProfileRepository.findById(designerProfileId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + designerProfileId));
        return designerProfileMapper.INSTANCE.toDesignerProfileResponse(designerProfile);
    }

    private DesignerProfile findDesignerProfileById(Long designerProfileId) {
        return designerProfileRepository.findById(designerProfileId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + designerProfileId));
    }
}

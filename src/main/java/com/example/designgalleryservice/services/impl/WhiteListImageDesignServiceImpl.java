package com.example.designgalleryservice.services.impl;

import com.example.designgalleryservice.entities.WhiteListImageDesign;
import com.example.designgalleryservice.entities.WhiteListImageDesignId;
import com.example.designgalleryservice.repositories.WhiteListImageDesignRepository;
import com.example.designgalleryservice.services.WhiteListImageDesignService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhiteListImageDesignServiceImpl implements WhiteListImageDesignService {
    private final WhiteListImageDesignRepository whiteListImageDesignRepository;

    @Override
    public List<WhiteListImageDesign> getAllWhiteListImageDesign() {
        return whiteListImageDesignRepository.findAll().stream().toList();
    }

    @Override
    public Optional<WhiteListImageDesign> getWhiteListImageDesignById(WhiteListImageDesignId id) {
        return whiteListImageDesignRepository.findById(id);
    }

    @Override
    public List<WhiteListImageDesign> getWhiteListImageDesignByUserId(Long userId) {
        return whiteListImageDesignRepository.findAllByUserId(userId);
    }

    @Override
    public Long getCountWhiteListImageDesign(Long userId) {
        return whiteListImageDesignRepository.findAllByUserId(userId).stream().count();
    }

    @Override
    public Long getWhiteListImageDesignByImageDesignId(Long imageDesignId) {
        return whiteListImageDesignRepository.findAllByImagesDesignId(imageDesignId).stream().count();
    }

    @Override
    public String addWhiteListImageDesign(WhiteListImageDesignId id) {
        var whiteList = getWhiteListImageDesignById(id);
        if (whiteList.isPresent()) {
            deleteWhiteListImageDesign(id);
            return "Delete White List Image Design Successfully!";
        }
        whiteListImageDesignRepository.save(WhiteListImageDesign.builder().id(id).build());
        return "Add White List Image Design Successfully!";
    }

    @Override
    public void deleteWhiteListImageDesign(WhiteListImageDesignId id) {
        whiteListImageDesignRepository.deleteById(id);
    }
}

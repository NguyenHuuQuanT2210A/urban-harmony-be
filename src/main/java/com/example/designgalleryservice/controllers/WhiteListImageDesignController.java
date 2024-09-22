package com.example.designgalleryservice.controllers;


import com.example.designgalleryservice.dto.response.ApiResponse;
import com.example.designgalleryservice.entities.WhiteListImageDesign;
import com.example.designgalleryservice.entities.WhiteListImageDesignId;
import com.example.designgalleryservice.services.WhiteListImageDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/white_list_image_design")
public class WhiteListImageDesignController {
    private final WhiteListImageDesignService whiteListImageDesignService;

    @GetMapping
    ApiResponse<List<WhiteListImageDesign>> getAll() {
        return ApiResponse.<List<WhiteListImageDesign>>builder()
                .message("Get all white lists image design")
                .data(whiteListImageDesignService.getAllWhiteListImageDesign())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<WhiteListImageDesign>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<WhiteListImageDesign>>builder()
                .message("Get white lists image design by user id")
                .data(whiteListImageDesignService.getWhiteListImageDesignByUserId(userId))
                .build();
    }

    @GetMapping("/user/{userId}/count")
    ApiResponse<Long> getCountByUserId(@PathVariable Long userId) {
        return ApiResponse.<Long>builder()
                .message("Get count white lists image design by user id")
                .data(whiteListImageDesignService.getCountWhiteListImageDesign(userId))
                .build();
    }

    @GetMapping("/imageDesign/{imageDesignId}")
    ApiResponse<Long> getByImageDesignId(@PathVariable Long imageDesignId) {
        return ApiResponse.<Long>builder()
                .message("Get white lists image design by product id")
                .data(whiteListImageDesignService.getWhiteListImageDesignByImageDesignId(imageDesignId))
                .build();
    }

    @PostMapping
    ApiResponse<String> createWhiteListImageDesign(@RequestBody WhiteListImageDesignId id) {
        return ApiResponse.<String>builder()
                .code(HttpStatus.CREATED.value())
                .message("Created white list image design")
                .data(whiteListImageDesignService.addWhiteListImageDesign(id))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteById(@RequestBody WhiteListImageDesignId id) {
        whiteListImageDesignService.deleteWhiteListImageDesign(id);
        return ApiResponse.<String>builder()
                .message("Delete WhiteListImageDesign Successfully")
                .build();
    }
}

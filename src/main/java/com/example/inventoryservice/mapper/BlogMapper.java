package com.example.inventoryservice.mapper;

import com.example.inventoryservice.dto.request.BlogRequest;
import com.example.inventoryservice.dto.response.BlogResponse;
import com.example.inventoryservice.entities.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogResponse toBlogResponse(Blog blog);

    Blog toBlog(BlogRequest request);
    void updatedBlog(@MappingTarget Blog blog, BlogRequest request);
}

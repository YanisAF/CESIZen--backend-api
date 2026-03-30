package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.category.CategoryDtoResponse;
import com.example.CESIZen.dto.page.ContentPageDto;
import com.example.CESIZen.dto.page.PageDtoRequest;
import com.example.CESIZen.dto.page.PageDtoResponse;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.model.page.ContentPage;
import com.example.CESIZen.model.page.Page;
import com.example.CESIZen.service.category.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PageMapper {
    private final CategoryService categoryService;

    public PageMapper(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Page toPage(PageDtoRequest dto, MultipartFile image) throws IOException {
        if (dto == null) return null;

        Page page = new Page();
        page.setTitle(dto.getTitle());

        Category category = categoryService.getCategoryById(dto.getCategory());
        page.setCategory(category);

        if (dto.getContent() != null) {
            List<ContentPage> contents = dto.getContent().stream()
                    .map(c -> {
                        ContentPage content = new ContentPage();
                        content.setName(c.getName());
                        content.setDescription(c.getDescription());
                        content.setItem(c.getItemUrl());
                        content.setPage(page);
                        return content;
                    })
                    .collect(Collectors.toList());

            page.setContent(contents);
        }

        if (image != null && !image.isEmpty()) {
            String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/pages");
            Files.createDirectories(uploadPath);
            Files.copy(image.getInputStream(), uploadPath.resolve(filename));
            page.setImageUrl("/uploads/pages/" + filename);
        }
        System.out.println("Category ID: " + category.getId());
        System.out.println("Page content size: " + page.getContent().size());
        return page;
    }

    public static PageDtoResponse toDto(Page page){
        if (page != null){
            PageDtoResponse pageDtoResponse = new PageDtoResponse();
            pageDtoResponse.setId(page.getId());
            pageDtoResponse.setTitle(page.getTitle());

            pageDtoResponse.setImageUrl(page.getImageUrl());

            List<ContentPageDto> contentDtos = page.getContent().stream()
                    .map(c -> {
                        ContentPageDto dto = new ContentPageDto();
                        dto.setName(c.getName());
                        dto.setDescription(c.getDescription());
                        dto.setItemUrl(c.getItem());
                        return dto;
                    }).collect(Collectors.toList());

            pageDtoResponse.setContent(contentDtos);

            Category category = page.getCategory();
            if (category != null) {
                CategoryDtoResponse categoryDto = new CategoryDtoResponse();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());
                pageDtoResponse.setCategory(categoryDto);
            }

            return pageDtoResponse;
        }
        return null;
    }
}

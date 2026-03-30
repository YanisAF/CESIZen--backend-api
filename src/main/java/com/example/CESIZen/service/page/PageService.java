package com.example.CESIZen.service.page;

import com.example.CESIZen.dto.page.PageDtoRequest;
import com.example.CESIZen.dto.page.PageDtoResponse;
import com.example.CESIZen.exception.PageNotFoundException;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.mapper.PageMapper;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.model.page.ContentPage;
import com.example.CESIZen.model.page.Page;
import com.example.CESIZen.repository.CategoryRepository;
import com.example.CESIZen.repository.PageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;

    public PageService(PageRepository pageRepository,
                       PageMapper pageMapper,
                       CategoryRepository categoryRepository
    ) {
        this.pageRepository = pageRepository;
        this.pageMapper = pageMapper;
        this.categoryRepository = categoryRepository;
    }

    public void createPage(PageDtoRequest pageDtoRequest, MultipartFile image) throws IOException {
        Page page = pageMapper.toPage(pageDtoRequest, image);
        pageRepository.save(page);
    }

    public PageDtoResponse getPageById(Integer id){
        return pageRepository.findById(id)
                .map(PageMapper::toDto)
                .orElse(null);
    }

    public PageDtoResponse getPageWithTitle(String title){
        Page page = pageRepository.findByTitle(title);
        return PageMapper.toDto(page);
    }

    public List<PageDtoResponse> getAllPages(){
        List<Page> pageList = pageRepository.findAll();
        return pageList.stream()
                .map(PageMapper::toDto)
                .toList();
    }

    public Page addContentPage(Integer id, ContentPage contentPage) throws PageNotFoundException{
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new PageNotFoundException(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        "page not found",
                        "/api/v1/..."));
        return pageRepository.save(page);
    }

    public PageDtoResponse updatePage(Integer id, PageDtoRequest pageRequest, MultipartFile image) throws ResourceNotFoundException, IOException {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Page not found: " + id));

        page.setTitle(pageRequest.getTitle());

        Category category = categoryRepository.findById(pageRequest.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + pageRequest.getCategory()));
        page.setCategory(category);

        if (pageRequest.getContent() != null) {
            page.getContent().clear();
            List<ContentPage> newContent = pageRequest.getContent().stream()
                    .map(dto -> {
                        ContentPage cp = new ContentPage();
                        cp.setName(dto.getName());
                        cp.setDescription(dto.getDescription());
                        cp.setItem(dto.getItemUrl());
                        cp.setPage(page);
                        return cp;
                    })
                    .toList();
            page.getContent().addAll(newContent);
        }

        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path uploadDir = Paths.get("uploads/pages");
            Files.createDirectories(uploadDir);
            Files.copy(image.getInputStream(), uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            page.setImageUrl("/uploads/pages/" + fileName);
        }

        return PageMapper.toDto(pageRepository.save(page));
    }

    public void deleteById(Integer id){
        pageRepository.deleteById(id);
    }
}

package com.example.CESIZen.controller.page;

import com.example.CESIZen.assembler.PageModelAssembler;
import com.example.CESIZen.dto.page.PageDtoRequest;
import com.example.CESIZen.dto.page.PageDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.page.PageService;
import com.example.CESIZen.utils.Routes;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PageController {

    private final PageService pageService;
    private final PageModelAssembler pageModelAssembler;

    public PageController(PageService pageService, PageModelAssembler pageModelAssembler) {
        this.pageService = pageService;
        this.pageModelAssembler = pageModelAssembler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            value = Routes.CREATE_PAGE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PageDtoResponse> createPage(
            @RequestPart("page") PageDtoRequest pageDtoRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        pageService.createPage(pageDtoRequest, image);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = Routes.UPDATE_PAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PageDtoResponse> updatePage(
            @RequestParam("id") Integer id,
            @RequestPart("page") PageDtoRequest pageRequest,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException, ResourceNotFoundException {

        return ResponseEntity.ok(pageService.updatePage(id, pageRequest, image));
    }

    @GetMapping(Routes.GET_PAGE)
    public ResponseEntity<PageDtoResponse> getPage(@RequestParam("id") Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageService.getPageById(id));
    }

    @GetMapping(Routes.GET_ALL_PAGE)
    public ResponseEntity<List<EntityModel<PageDtoResponse>>> getAllPages(){
        List<EntityModel<PageDtoResponse>> pageList = pageService.getAllPages()
                .stream()
                .map(pageModelAssembler::toModel)
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(Routes.DELETE_PAGE)
    public ResponseEntity<Void> deletePageById(@RequestParam("id") Integer id){
        pageService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

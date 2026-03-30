package com.example.CESIZen.controller.category;

import com.example.CESIZen.dto.category.CategoryDtoRequest;
import com.example.CESIZen.dto.category.CategoryDtoResponse;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.service.category.CategoryService;
import com.example.CESIZen.utils.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(Routes.CATEGORY_LIST)
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Routes.CREATE_CATEGORY)
    public ResponseEntity<CategoryDtoResponse> create(@RequestBody CategoryDtoRequest categoryDtoRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(categoryDtoRequest));
    }
}

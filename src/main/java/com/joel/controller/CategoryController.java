package com.joel.controller;

import com.joel.config.AppConstant;
import com.joel.model.Category;
import com.joel.payload.CategoryDto;
import com.joel.payload.CategoryRequestDto;
import com.joel.payload.CategoryResponseDto;
import com.joel.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponseDto> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);

    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryRequestDto category, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.updateCategory(category, categoryId), HttpStatus.OK);
    }
}

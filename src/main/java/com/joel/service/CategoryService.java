package com.joel.service;

import com.joel.model.Category;
import com.joel.payload.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}

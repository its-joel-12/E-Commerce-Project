package com.joel.service;

import com.joel.payload.CategoryDto;
import com.joel.payload.CategoryRequestDto;
import com.joel.payload.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDto createCategory(CategoryRequestDto categoryRequestDto);
    String deleteCategory(Long categoryId);
    CategoryDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId);
}

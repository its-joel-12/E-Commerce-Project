package com.joel.service;

import com.joel.model.Category;
import com.joel.payload.CategoryDto;
import com.joel.payload.CategoryRequestDto;
import com.joel.payload.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryDto createCategory(CategoryRequestDto categoryRequestDto);
    String deleteCategory(Long categoryId);
    CategoryDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId);
}

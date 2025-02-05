package com.joel.service;

import com.joel.exception.ApiException;
import com.joel.exception.ResourceNotFoundException;
import com.joel.model.Category;
import com.joel.payload.CategoryDto;
import com.joel.payload.CategoryResponseDto;
import com.joel.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDto getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories present in the database !");
        }

        List<CategoryDto> categoryDtoList = categories
                .stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .toList();

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setContent(categoryDtoList);
        return categoryResponseDto;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null){
            throw new ApiException("Category with name '" + category.getCategoryName() + "' already exists !");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));

        categoryRepository.delete(existingCategory);

        return "Category with categoryId: " + categoryId + " deleted successfully !";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        Category existingCategory = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));

        category.setCategoryId(categoryId);

        return categoryRepository.save(category);
    }
}

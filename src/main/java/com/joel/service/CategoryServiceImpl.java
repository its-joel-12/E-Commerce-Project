package com.joel.service;

import com.joel.exception.ApiException;
import com.joel.exception.ResourceNotFoundException;
import com.joel.model.Category;
import com.joel.payload.CategoryDto;
import com.joel.payload.CategoryRequestDto;
import com.joel.payload.CategoryResponseDto;
import com.joel.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public CategoryResponseDto getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Category> pageCategories = categoryRepository.findAll(pageable);
        List<Category> categories = pageCategories.getContent();

        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories present in the database !");
        }

        List<CategoryDto> categoryDtoList = categories
                .stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .toList();

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setContent(categoryDtoList);
        categoryResponseDto.setPageNumber(pageCategories.getNumber());
        categoryResponseDto.setPageSize(pageCategories.getSize());
        categoryResponseDto.setTotalElements((int) pageCategories.getTotalElements());
        categoryResponseDto.setTotalPages(pageCategories.getTotalPages());
        categoryResponseDto.setLastPage(pageCategories.isLast());
        return categoryResponseDto;
    }

    @Override
    public CategoryDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category savedCategory = categoryRepository.findByCategoryName(categoryRequestDto.getCategoryName());
        if(savedCategory != null){
            throw new ApiException("Category with name '" + categoryRequestDto.getCategoryName() + "' already exists !");
        }

        Category save = categoryRepository.save(modelMapper.map(categoryRequestDto, Category.class));
        return modelMapper.map(save, CategoryDto.class);
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
    public CategoryDto updateCategory(CategoryRequestDto categoryRequestDto, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryRequestDto.getCategoryName());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }
}

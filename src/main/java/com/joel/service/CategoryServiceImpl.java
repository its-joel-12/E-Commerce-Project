package com.joel.service;

import com.joel.exception.ApiException;
import com.joel.exception.ResourceNotFoundException;
import com.joel.model.Category;
import com.joel.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    //    private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories present in the database !");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null){
            throw new ApiException("Category with name '" + category.getCategoryName() + "' already exists !");
        }
//        category.setCategoryId(nextId++);
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

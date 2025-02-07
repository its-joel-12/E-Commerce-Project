package com.joel.service;

import com.joel.exception.ResourceNotFoundException;
import com.joel.model.Category;
import com.joel.model.Product;
import com.joel.model.ProductRepository;
import com.joel.payload.ProductDto;
import com.joel.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(Product product, Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));
        product.setCategory(category);
        product.setProductImage("default.png");
        Double specialPrice = product.getProductPrice() - (product.getProductPrice() * product.getProductDiscount() / 100);
        product.setProductSpecPrice(specialPrice);
        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
}

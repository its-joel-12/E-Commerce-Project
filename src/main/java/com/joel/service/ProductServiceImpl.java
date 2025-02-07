package com.joel.service;

import com.joel.exception.ResourceNotFoundException;
import com.joel.model.Category;
import com.joel.model.Product;
import com.joel.payload.ProductDto;
import com.joel.payload.ProductRequestDto;
import com.joel.payload.ProductResponseDto;
import com.joel.repository.CategoryRepository;
import com.joel.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductRequestDto productRequestDto, Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));
        Product product = modelMapper.map(productRequestDto, Product.class);
        product.setCategory(category);
        product.setProductImage("default.png");
        Double specialPrice = product.getProductPrice() - (product.getProductPrice() * product.getProductDiscount() / 100);
        product.setProductSpecPrice(specialPrice);
        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductResponseDto getAllProducts() {
        List<Product> allProducts = productRepo.findAll();
        if(allProducts.isEmpty()){
            throw new ResourceNotFoundException("No products found");
        }
        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).collect(Collectors.toList());
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        return productResponseDto;
    }
}

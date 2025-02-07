package com.joel.service;

import com.joel.payload.ProductDto;
import com.joel.payload.ProductRequestDto;
import com.joel.payload.ProductResponseDto;

public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequestDto, Long categoryId);
    ProductResponseDto getAllProducts();

    ProductResponseDto getProductsByCategory(Long categoryId);

    ProductResponseDto getProductsByKeyword(String keyword);
}

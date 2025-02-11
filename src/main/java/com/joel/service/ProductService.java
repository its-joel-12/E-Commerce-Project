package com.joel.service;

import com.joel.payload.ProductDto;
import com.joel.payload.ProductRequestDto;
import com.joel.payload.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequestDto, Long categoryId);
    ProductResponseDto getAllProducts();

    ProductResponseDto getProductsByCategory(Long categoryId);

    ProductResponseDto getProductsByKeyword(String keyword);

    ProductDto updateProduct(ProductRequestDto productRequestDto, Long productId);

    String deleteProduct(Long productId);

    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;
}

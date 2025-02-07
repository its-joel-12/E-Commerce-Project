package com.joel.service;

import com.joel.model.Product;
import com.joel.payload.ProductDto;

public interface ProductService {
    ProductDto addProduct(Product product, Long categoryId);
}

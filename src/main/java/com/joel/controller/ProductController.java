package com.joel.controller;

import com.joel.model.Product;
import com.joel.payload.ProductDto;
import com.joel.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody Product product, @PathVariable Long categoryId){
        return new ResponseEntity<>(productService.addProduct(product, categoryId), HttpStatus.CREATED);
    }
}

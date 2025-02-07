package com.joel.controller;

import com.joel.payload.ProductDto;
import com.joel.payload.ProductRequestDto;
import com.joel.payload.ProductResponseDto;
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

    // SAVE PRODUCT
    @PostMapping("admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductRequestDto productRequestDto, @PathVariable Long categoryId){
        return new ResponseEntity<>(productService.addProduct(productRequestDto, categoryId), HttpStatus.CREATED);
    }

    // GET ALL PRODUCTS
    @GetMapping("public/products")
    public ResponseEntity<ProductResponseDto> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    // GET PRODUCTS BY CATEGORY
    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDto> getProductsByCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId), HttpStatus.OK);
    }

    // GET PRODUCTS BY KEYWORD
    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDto> getProductsByKeyword(@PathVariable String keyword){
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword), HttpStatus.OK);
    }
}

package com.joel.controller;

import com.joel.config.AppConstant;
import com.joel.payload.ProductDto;
import com.joel.payload.ProductRequestDto;
import com.joel.payload.ProductResponseDto;
import com.joel.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // SAVE PRODUCT
    @PostMapping("admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductRequestDto productRequestDto, @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.addProduct(productRequestDto, categoryId), HttpStatus.CREATED);
    }

    // GET ALL PRODUCTS
    @GetMapping("public/products")
    public ResponseEntity<ProductResponseDto> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    // GET PRODUCTS BY CATEGORY
    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDto> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    // GET PRODUCTS BY KEYWORD
    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDto> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    // UPDATE PRODUCT
    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductRequestDto productRequestDto, @PathVariable Long productId) {
        return new ResponseEntity<>(productService.updateProduct(productRequestDto, productId), HttpStatus.OK);
    }

    // DELETE PRODUCT
    @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    //UPLOAD IMAGE
    @PutMapping("admin/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, image), HttpStatus.OK);
    }
}

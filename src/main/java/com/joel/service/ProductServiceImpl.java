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

    @Override
    public ProductResponseDto getProductsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));
        List<Product> allProducts = productRepo.findByCategoryOrderByProductPriceAsc((category));

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).collect(Collectors.toList());
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        return productResponseDto;
    }

    @Override
    public ProductResponseDto getProductsByKeyword(String keyword) {
        List<Product> allProducts = productRepo.findByProductNameContainingIgnoreCase((keyword));
        if(allProducts.isEmpty()){
            throw new ResourceNotFoundException("No matching results founds !");
        }

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).collect(Collectors.toList());
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        return productResponseDto;
    }

    @Override
    public ProductDto updateProduct(ProductRequestDto productRequestDto, Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with the given id: " + productId));
        product.setProductId(productId);
        product.setProductName(productRequestDto.getProductName());
        product.setProductDesc(productRequestDto.getProductDesc());
        product.setProductQuantity(productRequestDto.getProductQuantity());
        product.setProductDiscount(productRequestDto.getProductDiscount());
        product.setProductPrice(productRequestDto.getProductPrice());
        product.setProductImage(productRequestDto.getProductImage());
        product.setProductSpecPrice(product.getProductPrice() - (product.getProductPrice() * product.getProductDiscount() / 100));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public String deleteProduct(Long productId) {

        Product existingProduct = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with the given id: " + productId));

        productRepo.delete(existingProduct);

        return "Product with product Id: " + productId + " deleted successfully !";
    }
}

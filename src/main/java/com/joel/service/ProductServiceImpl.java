package com.joel.service;

import com.joel.exception.ApiException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepo;
    private CategoryRepository categoryRepo;
    private ModelMapper modelMapper;
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo, ModelMapper modelMapper, FileService fileService) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    // ADD PRODUCT
    @Override
    public ProductDto addProduct(ProductRequestDto productRequestDto, Long categoryId) {
        Product existingProduct = productRepo.findByProductName(productRequestDto.getProductName());
        if(existingProduct != null){
            throw new ApiException("Product with name '" + productRequestDto.getProductName() + "' already exists !");
        }

        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));
        Product product = modelMapper.map(productRequestDto, Product.class);
        product.setCategory(category);
        product.setProductImage("default.png");
        Double specialPrice = product.getProductPrice() - (product.getProductPrice() * product.getProductDiscount() / 100);
        product.setProductSpecPrice(specialPrice);
        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    // GET ALL PRODUCTS
    @Override
    public ProductResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageable);
        List<Product> allProducts = pageProducts.getContent();

        if(allProducts.isEmpty()){
            throw new ResourceNotFoundException("No products found");
        }

        List<ProductDto> productDtoList = allProducts
                .stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        productResponseDto.setPageNumber(pageProducts.getNumber());
        productResponseDto.setPageSize(pageProducts.getSize());
        productResponseDto.setTotalElements((int) pageProducts.getTotalElements());
        productResponseDto.setTotalPages(pageProducts.getTotalPages());
        productResponseDto.setLastPage(pageProducts.isLast());
        return productResponseDto;
    }

    // GET PRODUCTS BY CATEGORY
    @Override
    public ProductResponseDto getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findByCategoryOrderByProductPriceAsc(category, pageable);
        List<Product> allProducts = pageProducts.getContent();

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        productResponseDto.setPageNumber(pageProducts.getNumber());
        productResponseDto.setPageSize(pageProducts.getSize());
        productResponseDto.setTotalElements((int) pageProducts.getTotalElements());
        productResponseDto.setTotalPages(pageProducts.getTotalPages());
        productResponseDto.setLastPage(pageProducts.isLast());
        return productResponseDto;
    }

    // GET PRODUCTS BY KEYWORD
    @Override
    public ProductResponseDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findByProductNameContainingIgnoreCase(keyword, pageable);
        List<Product> allProducts = pageProducts.getContent();

        if(allProducts.isEmpty()){
            throw new ResourceNotFoundException("No matching results founds !");
        }

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        productResponseDto.setPageNumber(pageProducts.getNumber());
        productResponseDto.setPageSize(pageProducts.getSize());
        productResponseDto.setTotalElements((int) pageProducts.getTotalElements());
        productResponseDto.setTotalPages(pageProducts.getTotalPages());
        productResponseDto.setLastPage(pageProducts.isLast());
        return productResponseDto;
    }

    // UDPATE PRODUCT
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

    // DELETE PRODUCT
    @Override
    public String deleteProduct(Long productId) {

        Product existingProduct = productRepo
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with the given id: " + productId));

        productRepo.delete(existingProduct);

        return "Product with product Id: " + productId + " deleted successfully !";
    }

    // UPDATE PRODUCT IMAGE
    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product existingProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with the given id: " + productId));
        String fileName = fileService.uploadImage(path, image);
        existingProduct.setProductImage(fileName);
        Product udpatedProduct = productRepo.save(existingProduct);
        return modelMapper.map(udpatedProduct, ProductDto.class);
    }
}

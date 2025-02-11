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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepo;
    private CategoryRepository categoryRepo;
    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

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
        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setContent(productDtoList);
        return productResponseDto;
    }

    @Override
    public ProductResponseDto getProductsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id: " + categoryId));
        List<Product> allProducts = productRepo.findByCategoryOrderByProductPriceAsc((category));

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
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

        List<ProductDto> productDtoList = allProducts.stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();
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

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        // get the product from db
        Product existingProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with the given id: " + productId));

        // upload image to the server
        // get the file name of the uploaded image
        String path = "images/";
        String fileName = uploadImage(path, image);

        // updating the new file name to the product
        existingProduct.setProductImage(fileName);

        // save updated product
        Product udpatedProduct = productRepo.save(existingProduct);

        // return product dto after mapping product to dto
        return modelMapper.map(udpatedProduct, ProductDto.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        // get filename of the original / current file
        String originalFileName= file.getOriginalFilename();

        // generate a unique filename
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.'))); // joe.jpeg --> <uuid>.jpeg
        String filePath = path + File.separator + fileName;

        // check if path exists else create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        // upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // returning filename
        return fileName;
    }
}

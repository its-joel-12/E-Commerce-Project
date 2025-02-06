package com.joel.payload;

import com.joel.model.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String productName;
    private String productDesc;
    private Integer productQuantity;
    private Double productPrice;
    private Double productSpecPrice;
    private Double productDiscount;
    private String productImage;
}

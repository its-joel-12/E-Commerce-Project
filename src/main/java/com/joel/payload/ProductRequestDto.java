package com.joel.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequestDto {
    private String productName;
    private String productDesc;
    private Integer productQuantity;
    private Double productDiscount;
    private Double productPrice;
    private String productImage;
}

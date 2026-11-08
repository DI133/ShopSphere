package com.project.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDetailsDTO {
    private Integer user_id;
    private String username;
    private String email;
    private List<String> categories;
    private List<SellerProductDTO> products;
    private Integer totalProducts;
    private Integer totalCategories;
}
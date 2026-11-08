package com.project.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsDTO {

    private Integer user_id;
    private String username;
    private String email;

    private List<CustomerPurchaseDTO> purchases;

    private Integer totalPurchases;
}
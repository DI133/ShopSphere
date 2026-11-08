package com.project.ecommerce_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerOrderItemDto {

    private Integer itemId;
    private String productName;
    private int quantity;

    private String customerName;
    private String customerEmail;

    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String phone;
}

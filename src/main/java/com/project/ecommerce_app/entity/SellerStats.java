package com.project.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller_stats")
public class SellerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or AUTO
    private Integer seller_id;

    private Double totalRevenue=0.0;
    private Integer totalOrders = 0;
    private Integer totalItemsSold = 0;

    @ElementCollection
    @CollectionTable(name = "top_selling_products", joinColumns = @JoinColumn(name = "seller_id"))
    private List<TopProductEntry> topSellingProducts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}

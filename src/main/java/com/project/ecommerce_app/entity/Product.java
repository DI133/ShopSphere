package com.project.ecommerce_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    @Column(nullable=false)
    private String name;

    private String description;
    private Double price;
    private Integer stock;
    private Integer reviewsCount = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "category_id", nullable= false)
    //@JsonBackReference
    private Category category;


    @OneToMany(mappedBy="product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("product-images")
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

package com.project.ecommerce_app.repository;

import com.project.ecommerce_app.entity.Cart;
import com.project.ecommerce_app.entity.CartItem;
import com.project.ecommerce_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
//    Optional<CartItem> findById(Integer cart_item_id);
//      List<CartItem> findByCart_Cart_id(Integer cartId);
//    void deleteByCartId(Integer cartId);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByProduct(Product product);
}

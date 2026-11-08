package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.Cart;
import com.project.ecommerce_app.entity.CartItem;
import com.project.ecommerce_app.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // get the user's cart by userId
    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Cart>> getCart(@PathVariable Integer userId){
        return ResponseEntity.ok(Optional.ofNullable(cartService.getCartByUserId(userId)));
    }

    // add product to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") int quantity) {
        cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    //to get cart items of a user by userId
    @GetMapping("/items/{userId}")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable Integer userId){
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }
}

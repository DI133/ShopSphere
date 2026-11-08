package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.CartItem;
import com.project.ecommerce_app.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart-item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    // add a cart item
    @PostMapping("/add")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartItemService.addCartItem(cartItem));
    }


    // get a cart item by ID
    @GetMapping("/{cartItemId}")
    public ResponseEntity<Optional<CartItem>> getCartItemById(@PathVariable Integer cartItemId) {
        return ResponseEntity.ok(cartItemService.getCartItemById(cartItemId));
    }

    // remove a cart item
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Integer cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    // increase quantity (+ button)
    @PutMapping("/{cartItemId}/increase")
    public ResponseEntity<Void> increaseQuantity(@PathVariable Integer cartItemId) {
        cartItemService.increaseQuantity(cartItemId);
        return ResponseEntity.ok().build();
    }

    // decrease quantity (- button)
    @PutMapping("/{cartItemId}/decrease")
    public ResponseEntity<Void> decreaseQuantity(@PathVariable Integer cartItemId) {
        cartItemService.decreaseQuantity(cartItemId);
        return ResponseEntity.ok().build();
    }
}
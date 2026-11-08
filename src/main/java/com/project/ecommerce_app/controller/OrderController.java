package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.dto.AddressDto;
import com.project.ecommerce_app.dto.SellerOrderItemDto;
import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.OrderItem;
import com.project.ecommerce_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // place an order (not used) (see in payment controller)
    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Integer userId, @RequestBody AddressDto address) {
        return ResponseEntity.ok(orderService.placeOrder(userId, address));
    }

    // get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // get all orders of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // cancel order
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order Cancelled");
    }

    // see pending orders (used by seller)
    @GetMapping("/orderitems/pending/{sellerId}")
    public ResponseEntity<List<SellerOrderItemDto>> getNewOrderItemsForSeller(@PathVariable Integer sellerId){
        List<SellerOrderItemDto> items = orderService.getNewOrderItemsForSeller(sellerId);
        return ResponseEntity.ok(items);
    }

    // see completed orders (used by seller)
    @GetMapping("/orderitems/completed/{sellerId}")
    public ResponseEntity<List<SellerOrderItemDto>> getPastOrderItemsForSeller(@PathVariable Integer sellerId){
        List<SellerOrderItemDto> items = orderService.getPastOrderItemsForSeller(sellerId);
        return ResponseEntity.ok(items);
    }

    //mark order item as completed
    @GetMapping("/markascomplete/{orderItemId}")
    public ResponseEntity<String> markOrderItemAsCompleted(@PathVariable Integer orderItemId) {
        String result = orderService.markOrderItemAsCompleted(orderItemId);
        return ResponseEntity.ok(result);
    }
}

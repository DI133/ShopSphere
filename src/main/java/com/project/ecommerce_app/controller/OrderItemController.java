package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.OrderItem;
import com.project.ecommerce_app.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // add an order item to an order
    @PostMapping("/{orderId}")
    public ResponseEntity<OrderItem> addOrderItem(@PathVariable Integer orderId, @RequestBody OrderItem orderItem) {

        OrderItem savedOrderItem = orderItemService.addOrderItem(orderId, orderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderItem);
    }

    // get all order items by orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Integer orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    // remove an order item by orderItemId
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Integer orderItemId) {
        orderItemService.removeOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}

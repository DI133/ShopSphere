package com.project.ecommerce_app.service;

import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.OrderItem;
import com.project.ecommerce_app.repository.OrderItemRepository;
import com.project.ecommerce_app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderItem addOrderItem(Integer orderId, OrderItem orderItem) {
        // Associate the order with the orderItem
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderItem.setOrder(order);
        return orderItemRepository.save(orderItem);
    }

    @Override
    public Optional<OrderItem> getOrderItemById(Integer orderItemId) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        // Fetch order by orderId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Get order items related to the order
        return orderItemRepository.findByOrder(order);
    }

    @Override
    public void removeOrderItem(Integer orderItemId) {
        // Check if the order item exists
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new RuntimeException("Order item not found");
        }

        // Delete order item
        orderItemRepository.deleteById(orderItemId);
    }

}

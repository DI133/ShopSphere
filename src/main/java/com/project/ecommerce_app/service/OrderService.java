package com.project.ecommerce_app.service;

import com.project.ecommerce_app.dto.*;
import com.project.ecommerce_app.dto.SellerOrderItemDto;
import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order placeOrder(Integer UserId, AddressDto address);
    Optional<Order> getOrderById(Integer orderId);
    List<Order> getAllOrders();
    void cancelOrder(Integer orderId);
    List<Order> getOrdersByUserId(Integer userId);
    List<SellerOrderItemDto> getNewOrderItemsForSeller(Integer sellerId);
    List<SellerOrderItemDto> getPastOrderItemsForSeller(Integer sellerId);
    String markOrderItemAsCompleted(Integer orderItemId);
}

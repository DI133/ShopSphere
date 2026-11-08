package com.project.ecommerce_app.service;

import com.project.ecommerce_app.dto.*;
import com.project.ecommerce_app.entity.*;
import com.project.ecommerce_app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private  ProductRepository productRepository;

    @Override
    @Transactional
    public Order placeOrder(Integer userId, AddressDto address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Cart cart = cartRepository.findByUser(user);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // Create a new Order
        Order order = new Order();
        order.setUser(cart.getUser()); // Assign user
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.pending);
        order.setCity(address.city);
        order.setState(address.state);
        order.setStreet(address.street);
        order.setZipcode(address.zipcode);
        order.setPhone(address.phone);
        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {

            Product product = cartItem.getProduct();
            if(product.getStock() < cartItem.getQuantity()){
                throw new RuntimeException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            //reduce stock by added quantity

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());

        // Set order total price
        double totalAmount = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalAmount(totalAmount);

        // Save order and order items
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);


        return order;
    }

    @Override
    @Transactional
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    @Override
    @Transactional
    public void cancelOrder(Integer orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Restore stock for each ordered item
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            int restoredStock = product.getStock() + item.getQuantity();
            product.setStock(restoredStock);

            // update product stock
            productRepository.save(product);

            // update analytics (remove sales effect)
            analyticsService.recalculateSellerStats(product.getUser().getUser_id());
        }

        // delete the order
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public List<Order> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return orderRepository.findByUser(user);
    }


    @Override
    @Transactional
    public List<SellerOrderItemDto> getNewOrderItemsForSeller(Integer sellerId) {

        List<OrderItem> items = orderItemRepository
                .findPendingOrderItemsBySellerId(sellerId);

        return items.stream().map(item -> {

            Order order = item.getOrder();
            User user = order.getUser();

            return new SellerOrderItemDto(
                    item.getItem_id(),
                    item.getProduct().getName(),
                    item.getQuantity(),

                    user.getUsername(),
                    user.getEmail(),

                    order.getStreet(),
                    order.getCity(),
                    order.getState(),
                    order.getZipcode(),
                    order.getPhone()
            );

        }).toList();
    }


    @Override
    @Transactional
    public List<SellerOrderItemDto> getPastOrderItemsForSeller(Integer sellerId){
        List<OrderItem> items = orderItemRepository
                .findCompletedOrderItemsBySellerId(sellerId);

        return items.stream().map(item -> {

            Order order = item.getOrder();
            User user = order.getUser();

            return new SellerOrderItemDto(
                    item.getItem_id(),
                    item.getProduct().getName(),
                    item.getQuantity(),

                    user.getUsername(),
                    user.getEmail(),

                    order.getStreet(),
                    order.getCity(),
                    order.getState(),
                    order.getZipcode(),
                    order.getPhone()
            );

        }).toList();
    }

    @Override
    @Transactional
    public String markOrderItemAsCompleted(Integer orderItemId) {

        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // mark item complete
        item.setComplete(true);
        orderItemRepository.save(item);

        analyticsService.updateSellerStats(orderItemId);

        Order order = item.getOrder();

        // fetch all items of this order
        List<OrderItem> items = orderItemRepository.findByOrder(order);

        boolean allCompleted = items.stream()
                .allMatch(i -> Boolean.TRUE.equals(i.isComplete()));

        if (allCompleted) {
            order.setStatus(OrderStatus.delivered);
            orderRepository.save(order);
        }
        return "";
    }

}


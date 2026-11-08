package com.project.ecommerce_app.service;

import com.project.ecommerce_app.entity.*;
import com.project.ecommerce_app.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerStatsRepository sellerStatsRepository;
    public void calculateAndSaveSellerStats(Integer sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Order> deliveredOrders = orderRepository.findByStatus(OrderStatus.delivered);

        double totalRevenue = 0;
        int totalOrders = 0;
        int totalItemsSold = 0;

        Map<Integer, Integer> productSalesCount = new HashMap<>();

        for (Order order : deliveredOrders) {
            boolean orderHasSellerProduct = false;

            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product.getUser().getUser_id().equals(sellerId)) {
                    orderHasSellerProduct = true;
                    int quantity = item.getQuantity();
                    double price = item.getPrice();

                    totalRevenue += quantity * price;
                    totalItemsSold += quantity;

                    int productId = product.getProduct_id();
                    productSalesCount.put(productId,
                            productSalesCount.getOrDefault(productId, 0) + quantity);
                }
            }

            if (orderHasSellerProduct) {
                totalOrders++;
            }
        }

        // Top 3 selling products
        List<TopProductEntry> topSelling = productSalesCount.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue()) // Descending
                .limit(3)
                .map(e -> new TopProductEntry(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        SellerStats stats = sellerStatsRepository.findByUser(seller).orElse(null);

        if (stats == null) {
            stats = new SellerStats();
            stats.setUser(seller);
        }

        stats.setTotalRevenue(totalRevenue);
        stats.setTotalOrders(totalOrders);
        stats.setTotalItemsSold(totalItemsSold);
        stats.setTopSellingProducts(topSelling);

        sellerStatsRepository.save(stats);
    }


    @Transactional
    public void updateSellerStats(Integer orderItemId) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        Product product = item.getProduct();
        User seller = product.getUser();
        Integer sellerId = seller.getUser_id();
        Order order = item.getOrder();

        SellerStats stats = sellerStatsRepository.findByUser(seller).orElse(null);
        if(stats == null){
            stats = new SellerStats();
            stats.setUser(seller); // make sure foreign key is populated
            stats = sellerStatsRepository.save(stats);
        }

        // Update revenue and items sold
        double revenue = item.getQuantity() * item.getPrice();
        stats.setTotalRevenue(stats.getTotalRevenue() + revenue);
        stats.setTotalItemsSold(stats.getTotalItemsSold() + item.getQuantity());

        // Ensure we only count one order per seller
//        if (!sellerStatsRepository.hasSellerSeenOrder(sellerId, order.getOrder_id())) {
        stats.setTotalOrders(stats.getTotalOrders() + 1);
//            sellerStatsRepository.markSellerOrderSeen(sellerId, order.getOrder_id()); // You need to implement this tracking logic
//        }

        // Update top-selling products
        List<TopProductEntry> topProducts = stats.getTopSellingProducts();
        boolean found = false;

        for (TopProductEntry entry : topProducts) {
            if (entry.getProductId().equals(product.getProduct_id())) {
                entry.setQuantitySold(entry.getQuantitySold() + item.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            topProducts.add(new TopProductEntry(product.getProduct_id(), item.getQuantity()));
        }

        // Sort and trim top 3 products
        topProducts.sort((a, b) -> b.getQuantitySold() - a.getQuantitySold());
        if (topProducts.size() > 3) {
            stats.setTopSellingProducts(topProducts.subList(0, 3));
        }

        sellerStatsRepository.save(stats);
    }

    @Transactional
    public void recalculateSellerStats(Integer sellerId) {
        // recompute stats from scratch
        calculateAndSaveSellerStats(sellerId);
    }


    public SellerStats getSellerStats(Integer userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        return sellerStatsRepository.findByUser(seller)
                .orElseThrow(() -> new RuntimeException("Seller stats not found for user ID: " + userId));
    }


}


//    @Transactional
//    void UpdateSellerStats(Order order){
//        Map<Integer, Set<Integer>> sellerOrderTracker = new HashMap<>();
//        for (OrderItem item : order.getOrderItems()) {
//            Product product = item.getProduct();
//            User seller = product.getUser();
//            Integer sellerId = seller.getUser_id();
//
//            SellerStats stats = sellerStatsRepository.findByUser(seller)
//                    .orElseGet(() -> sellerStatsRepository.save(new SellerStats(seller))); // fallback for first-time
//
//            // Update values
//            double revenue = item.getQuantity() * item.getPrice();
//            stats.setTotalRevenue(stats.getTotalRevenue() + revenue);
//            stats.setTotalItemsSold(stats.getTotalItemsSold() + item.getQuantity());
//
//            // Avoid incrementing order count multiple times for same seller
//            sellerOrderTracker.putIfAbsent(sellerId, new HashSet<>());
//            if (!sellerOrderTracker.get(sellerId).contains(order.getOrder_id())) {
//                stats.setTotalOrders(stats.getTotalOrders() + 1);
//                sellerOrderTracker.get(sellerId).add(order.getOrder_id());
//            }
//
//            // Update product sales count
//            List<TopProductEntry> topProducts = stats.getTopSellingProducts();
//            boolean found = false;
//            for (TopProductEntry entry : topProducts) {
//                if (entry.getProductId().equals(product.getProduct_id())) {
//                    entry.setQuantitySold(entry.getQuantitySold() + item.getQuantity());
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                topProducts.add(new TopProductEntry(product.getProduct_id(), item.getQuantity()));
//            }
//
//            // Sort and trim top 3
//            topProducts.sort((a, b) -> b.getQuantitySold() - a.getQuantitySold());
//            if (topProducts.size() > 3) {
//                stats.setTopSellingProducts(topProducts.subList(0, 3));
//            }
//
//            sellerStatsRepository.save(stats);
//        }
//
//    }

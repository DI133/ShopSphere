package com.project.ecommerce_app.repository;

import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.OrderItem;
import com.project.ecommerce_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder_User(User user);


    List<OrderItem> findByOrder(Order order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.order_id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);

    @Query("""
            SELECT oi 
            FROM OrderItem oi
            JOIN FETCH oi.order o
            JOIN FETCH o.user u
            WHERE oi.product.user.user_id = :sellerId         
            AND oi.complete = false
            """)
    List<OrderItem> findPendingOrderItemsBySellerId(@Param("sellerId") Integer sellerId);


    @Query("""
            SELECT oi       
            FROM OrderItem oi
            JOIN FETCH oi.order o
            JOIN FETCH o.user u
            WHERE oi.product.user.user_id = :sellerId           
            AND oi.complete = true
            """)
    List<OrderItem> findCompletedOrderItemsBySellerId(@Param("sellerId") Integer sellerId);
}

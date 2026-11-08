package com.project.ecommerce_app.repository;

import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.OrderStatus;
import com.project.ecommerce_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
//    List<Order> findByUser_User_id(Integer userId);

//    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems i WHERE i.product.user.user_id = :sellerId AND o.status = :status")
//    List<Order> findOrdersBySellerAndStatus(@Param("sellerId") Integer sellerId, @Param("status") OrderStatus status);
//
//    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems i WHERE i.product.user.user_id = :sellerId AND o.status != :status")
//    List<Order> findOrdersBySellerAndStatusNot(@Param("sellerId") Integer sellerId, @Param("status") OrderStatus status);

}

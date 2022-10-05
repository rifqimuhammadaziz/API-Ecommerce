package com.rifqimuhammadaziz.ecommerce.repository;

import com.rifqimuhammadaziz.ecommerce.entity.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findOrderByUserId(String userId, Pageable pageable);

    // filter orders by order number or user
    @Query("SELECT o FROM Order o WHERE LOWER(o.orderNumber) LIKE %:filterText% OR LOWER(o.user.fullName) LIKE %:filterText%")
    List<Order> search(@Param("filterText") String filterText, Pageable pageable);
}

package com.rifqimuhammadaziz.ecommerce.repository;

import com.rifqimuhammadaziz.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}

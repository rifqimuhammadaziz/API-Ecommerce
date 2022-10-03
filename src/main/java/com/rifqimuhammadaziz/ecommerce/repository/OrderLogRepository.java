package com.rifqimuhammadaziz.ecommerce.repository;

import com.rifqimuhammadaziz.ecommerce.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, String> {
}

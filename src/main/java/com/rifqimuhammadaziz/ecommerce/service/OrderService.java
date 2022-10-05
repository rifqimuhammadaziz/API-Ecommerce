package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Order;
import com.rifqimuhammadaziz.ecommerce.model.OrderRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(String username, OrderRequest request);
    Order cancelOrder(String orderId, String userId);
    Order acceptOrder(String orderId, String userId);
    List<Order> findAllUserOrder(String userId, int page, int limit);
    List<Order> search(String filterText, int page, int limit);
    Order paymentConfirmation(String orderId, String userId);
    Order orderPacking(String orderId, String userId);
    Order sendOrder(String orderId, String userId);
}

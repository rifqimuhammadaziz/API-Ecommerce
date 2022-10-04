package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.model.OrderRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderResponse;

public interface OrderService {
    OrderResponse create(String username, OrderRequest request);
}

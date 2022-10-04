package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Cart;

import java.util.List;

public interface CartService {
    Cart addCart(String username, String productId, Double quantity);
    Cart updateQuantity(String username, String productId, Double quantity);
    void deleteCart(String username, String productId);
    List<Cart> findCartByUserId(String username);
}

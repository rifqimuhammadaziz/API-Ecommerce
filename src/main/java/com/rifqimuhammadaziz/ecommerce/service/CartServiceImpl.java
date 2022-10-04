package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Cart;
import com.rifqimuhammadaziz.ecommerce.entity.Product;
import com.rifqimuhammadaziz.ecommerce.entity.User;
import com.rifqimuhammadaziz.ecommerce.exception.BadRequestException;
import com.rifqimuhammadaziz.ecommerce.repository.CartRepository;
import com.rifqimuhammadaziz.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Transactional
    @Override
    public Cart addCart(String username, String productId, Double quantity) {
        // check product is exists on database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product ID: " + productId + " is not found"));

        // check product is exists on cart
        Optional<Cart> optional = cartRepository.findCartByUserIdAndProductId(username, productId);
        Cart cart;

        if (optional.isPresent()) {
            // if exists, update the quantity
            cart = optional.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cartRepository.save(cart);
        } else {
            // if not, add new cart
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cart.setUser(new User(username));
            cartRepository.save(cart);
        }

        return cart;
    }

    @Transactional
    @Override
    public Cart updateQuantity(String username, String productId, Double quantity) {
        // get cart by username & product
        Cart cart = cartRepository.findCartByUserIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException("Product ID: " + productId + " is not found on your cart"));

        // set quantity & total
        cart.setQuantity(quantity);
        cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
        cartRepository.save(cart);

        return cart;
    }

    @Transactional
    @Override
    public void deleteCart(String username, String productId) {
        // get cart by username & product
        Cart cart = cartRepository.findCartByUserIdAndProductId(username, productId)
                .orElseThrow(() -> new BadRequestException("Product ID: " + productId + " is not found on your cart"));
        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> findCartByUserId(String username) {
        return cartRepository.findCartByUserId(username);
    }


}


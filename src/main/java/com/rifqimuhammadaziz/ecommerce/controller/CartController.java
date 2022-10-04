package com.rifqimuhammadaziz.ecommerce.controller;

import com.rifqimuhammadaziz.ecommerce.entity.Cart;
import com.rifqimuhammadaziz.ecommerce.model.CartRequest;
import com.rifqimuhammadaziz.ecommerce.security.service.UserDetailsImpl;
import com.rifqimuhammadaziz.ecommerce.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    @GetMapping("/carts")
    public List<Cart> findCartByUserId(@AuthenticationPrincipal UserDetailsImpl user) {
        return cartService.findCartByUserId(user.getUsername());
    }

    @PostMapping("/carts")
    public Cart addCart(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody CartRequest request) {
        return cartService.addCart(user.getUsername(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping("/carts/{productId}")
    public Cart updateCart(@AuthenticationPrincipal UserDetailsImpl user,
                           @PathVariable("productId") String productId,
                           @RequestParam("quantity") Double quantity) {
        return cartService.updateQuantity(user.getUsername(), productId, quantity);
    }

    @DeleteMapping("/carts/{productId}")
    public void deleteProductFromCart(@AuthenticationPrincipal UserDetailsImpl user,
                                      @PathVariable("productId") String productId) {
        cartService.deleteCart(user.getUsername(), productId);
    }

}

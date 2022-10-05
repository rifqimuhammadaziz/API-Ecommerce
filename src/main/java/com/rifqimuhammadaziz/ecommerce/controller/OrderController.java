package com.rifqimuhammadaziz.ecommerce.controller;

import com.rifqimuhammadaziz.ecommerce.entity.Order;
import com.rifqimuhammadaziz.ecommerce.entity.User;
import com.rifqimuhammadaziz.ecommerce.model.OrderRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderResponse;
import com.rifqimuhammadaziz.ecommerce.security.service.UserDetailsImpl;
import com.rifqimuhammadaziz.ecommerce.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('USER')")
    public OrderResponse createOrder(@AuthenticationPrincipal UserDetailsImpl user,
                                @RequestBody OrderRequest request) {
        return orderService.create(user.getUsername(), request);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('USER')")
    public Order cancelOrder(@AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable("orderId") String orderId) {
        return orderService.cancelOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasAuthority('USER')")
    public Order acceptOrder(@AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable("orderId") String orderId) {
        return orderService.acceptOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/confirmation")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order paymentConfirmationOrder(@AuthenticationPrincipal UserDetailsImpl user,
                        @PathVariable("orderId") String orderId) {
        return orderService.paymentConfirmation(user.getUsername(), orderId);
    }

    @PatchMapping("/orders/{orderId}/packing")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order packingOrder(@AuthenticationPrincipal UserDetailsImpl user,
                                          @PathVariable("orderId") String orderId) {
        return orderService.orderPacking(user.getUsername(), orderId);
    }

    @PatchMapping("/orders/{orderId}/send")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order sendOrder(@AuthenticationPrincipal UserDetailsImpl user,
                                          @PathVariable("orderId") String orderId) {
        return orderService.sendOrder(user.getUsername(), orderId);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('USER')")
    public List<Order> findAllUserOrder(@AuthenticationPrincipal UserDetailsImpl user,
                                        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                        @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return orderService.findAllUserOrder(user.getUsername(), page, limit);
    }

    @GetMapping("/orders/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Order> search(@AuthenticationPrincipal UserDetailsImpl user,
                                        @RequestParam(name = "filterText", defaultValue = "", required = false) String filterText,
                                        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                        @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return orderService.search(filterText, page, limit);
    }
}

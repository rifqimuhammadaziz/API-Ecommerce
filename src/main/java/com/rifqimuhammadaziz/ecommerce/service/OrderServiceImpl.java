package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.*;
import com.rifqimuhammadaziz.ecommerce.exception.BadRequestException;
import com.rifqimuhammadaziz.ecommerce.log.OrderLogService;
import com.rifqimuhammadaziz.ecommerce.model.CartRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderResponse;
import com.rifqimuhammadaziz.ecommerce.model.OrderStatus;
import com.rifqimuhammadaziz.ecommerce.repository.OrderItemRepository;
import com.rifqimuhammadaziz.ecommerce.repository.OrderRepository;
import com.rifqimuhammadaziz.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderLogService orderLogService;

    @Transactional
    @Override
    public OrderResponse create(String username, OrderRequest request) {
        // create order
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderDate(new Date());
        order.setOrderNumber(generateOrderNumber());
        order.setUser(new User(username));
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.DRAFT);
        order.setOrderTime(new Date());

        List<OrderItem> items = new ArrayList<>();

        for (CartRequest cartRequest : request.getItems()) {
            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product ID: " + cartRequest.getProductId() + " is not found"));
            // if stock < quantity request
            if (product.getStock() < cartRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock to make a transaction");
            }
            // create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProduct(product);
            orderItem.setDescription(product.getName());
            orderItem.setQuantity(cartRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotal(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getQuantity()));
            orderItem.setOrder(order);
            items.add(orderItem);
        }

        BigDecimal amount = BigDecimal.ZERO;
        for (OrderItem orderItem : items) {
            amount = amount.add(orderItem.getTotal());
        }

        order.setAmount(amount);
        order.setShippingCost(request.getShippingCost());
        order.setTotalPrice(order.getAmount().add(order.getShippingCost()));

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : items) {
            orderItemRepository.save(orderItem);
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepository.save(product);
            cartService.deleteCart(username, product.getId());
        }

        // order log
        orderLogService.createLog(username, order, OrderLogService.DRAFT, "Order success created");

        OrderResponse orderResponse = new OrderResponse(savedOrder, items);
        return orderResponse;
    }

    // must be unique to avoid duplicated orderNumber
    private String generateOrderNumber() {
        return String.format("%016d", System.nanoTime());
    }
}

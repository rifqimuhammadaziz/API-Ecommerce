package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.*;
import com.rifqimuhammadaziz.ecommerce.exception.BadRequestException;
import com.rifqimuhammadaziz.ecommerce.exception.ResourceNotFoundException;
import com.rifqimuhammadaziz.ecommerce.log.OrderLogService;
import com.rifqimuhammadaziz.ecommerce.model.CartRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderRequest;
import com.rifqimuhammadaziz.ecommerce.model.OrderResponse;
import com.rifqimuhammadaziz.ecommerce.model.OrderStatus;
import com.rifqimuhammadaziz.ecommerce.repository.OrderItemRepository;
import com.rifqimuhammadaziz.ecommerce.repository.OrderRepository;
import com.rifqimuhammadaziz.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional
    @Override
    public Order cancelOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " is not found"));

        // cancel order only by the user owner
        if (!userId.equals(order.getUser().getId())) {
            throw new BadRequestException("Can not cancel this order because the user is not the order's owner");
        }

        // order cancellation only for status draft
        if (!OrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BadRequestException("This order cannot be cancelled, because its being processed");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        orderLogService.createLog(userId, order, OrderLogService.CANCELLED, "Order has been cancelled ");

        return savedOrder;
    }

    @Override
    public Order acceptOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " is not found"));
        // accept order only by the user owner
        if (!userId.equals(order.getUser().getId())) {
            throw new BadRequestException("Can not accept this order because the user is not the order's owner");
        }

        // order accept only for status delivering
        if (!OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new BadRequestException("Failed to accept order because your order is on " + order.getStatus().name());
        }

        order.setStatus(OrderStatus.DELIVERED);
        Order savedOrder = orderRepository.save(order);
        orderLogService.createLog(userId, order, OrderLogService.DELIVERED, "Order has been delivered");
        return savedOrder;
    }

    @Override
    public List<Order> findAllUserOrder(String userId, int page, int limit) {
        return orderRepository.findOrderByUserId(userId, PageRequest.of(page, limit, Sort.by("orderDate").descending()));
    }

    @Override
    public List<Order> search(String filterText, int page, int limit) {
        return orderRepository.search(
                filterText.toLowerCase(),
                PageRequest.of(page, limit, Sort.by("order_date").descending()));
    }

    @Override
    public Order paymentConfirmation(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " is not found"));

        // order accept only for status delivering
        if (!OrderStatus.DRAFT.equals(order.getStatus())) {
            throw new BadRequestException("Failed to confirm payment because the status order is " + order.getStatus());
        }

        order.setStatus(OrderStatus.PAID);
        Order savedOrder = orderRepository.save(order);
        orderLogService.createLog(userId, order, OrderLogService.PAID, "Order has been paid");
        return savedOrder;
    }

    @Override
    public Order orderPacking(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " is not found"));

        // order packing only for status paid
        if (!OrderStatus.PAID.equals(order.getStatus())) {
            throw new BadRequestException("Failed to packing order because the status order is " + order.getStatus());
        }

        order.setStatus(OrderStatus.PACKING);
        Order savedOrder = orderRepository.save(order);
        orderLogService.createLog(userId, order, OrderLogService.PACKING, "Order is being prepared");
        return savedOrder;
    }

    @Override
    public Order sendOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID: " + orderId + " is not found"));

        // order send only for status packing
        if (!OrderStatus.PACKING.equals(order.getStatus())) {
            throw new BadRequestException("Failed to packing order because the status order is " + order.getStatus());
        }

        order.setStatus(OrderStatus.DELIVERING);
        Order savedOrder = orderRepository.save(order);
        orderLogService.createLog(userId, order, OrderLogService.DELIVERING, "Order is delivering");
        return savedOrder;    }

    // must be unique to avoid duplicated orderNumber
    private String generateOrderNumber() {
        return String.format("%016d", System.nanoTime());
    }
}

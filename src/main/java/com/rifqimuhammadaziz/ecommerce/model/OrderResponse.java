package com.rifqimuhammadaziz.ecommerce.model;

import com.rifqimuhammadaziz.ecommerce.entity.Order;
import com.rifqimuhammadaziz.ecommerce.entity.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse implements Serializable {
    private String id;
    private String orderNumber;
    private Date date;
    private String buyerName;
    private String shippingAddress;
    private Date orderDate;
    private BigDecimal amount;
    private BigDecimal shippingCost;
    private BigDecimal totalPrice;
    private List<Item> items;

    public OrderResponse(Order order, List<OrderItem> orderItems) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.date = order.getOrderDate();
        this.buyerName = order.getUser().getFullName();
        this.shippingAddress = order.getShippingAddress();
        this.orderDate = order.getOrderTime();
        this.amount = order.getAmount();
        this.shippingCost = order.getShippingCost();
        this.totalPrice = order.getTotalPrice();
        items = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Item item = new Item();
            item.setProductId(orderItem.getProduct().getId());
            item.setProductName(orderItem.getProduct().getName());
            item.setPrice(orderItem.getPrice());
            item.setQuantity(orderItem.getQuantity());
            item.setTotal(orderItem.getTotal());
            items.add(item);
        }
    }

    @Data
    public static class Item implements Serializable {
        private String productId;
        private String productName;
        private Double quantity;
        private BigDecimal price;
        private BigDecimal total;
    }
}

package com.rifqimuhammadaziz.ecommerce.entity;

import com.rifqimuhammadaziz.ecommerce.model.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    private String id;
    private String orderNumber;

    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @JoinColumn
    @ManyToOne
    private User user;
    private String shippingAddress;
    private BigDecimal amount;
    private BigDecimal shippingCost;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;
}

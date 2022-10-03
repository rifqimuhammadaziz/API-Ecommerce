package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "orderitems")
public class OrderItem implements Serializable {

    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Order order;

    @JoinColumn
    @ManyToOne
    private Product product;

    private String description;
    private Double quantity;
    private BigDecimal price;
    private BigDecimal total;
}

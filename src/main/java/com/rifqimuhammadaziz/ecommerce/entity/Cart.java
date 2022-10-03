package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "carts")
public class Cart implements Serializable {

    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Product product;

    @JoinColumn
    @ManyToOne
    private User user;

    private Double quantity;
    private BigDecimal price;
    private BigDecimal total;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}

package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product implements Serializable {

    @Id
    private String id;
    private String name;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn
    private Category category;

    private BigDecimal price;
    private Double stock;

}

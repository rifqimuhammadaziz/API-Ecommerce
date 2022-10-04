package com.rifqimuhammadaziz.ecommerce.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartRequest implements Serializable {
    private String productId;
    private Double quantity;
}

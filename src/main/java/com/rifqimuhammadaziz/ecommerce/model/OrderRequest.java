package com.rifqimuhammadaziz.ecommerce.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    private BigDecimal shippingCost;
    private String shippingAddress;
    private List<CartRequest> items;
}

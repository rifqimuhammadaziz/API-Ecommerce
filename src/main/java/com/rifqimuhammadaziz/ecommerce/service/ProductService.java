package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(String id);
    Product create(Product product);
    Product update(Product product);
    void deleteById(String id);

    Product changeImage(String id, String image);
}

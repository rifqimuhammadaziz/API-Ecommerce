package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(String id);
    Category create(Category category);
    Category update(Category category);
    void deleteById(String id);
}

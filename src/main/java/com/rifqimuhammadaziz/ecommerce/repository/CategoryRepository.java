package com.rifqimuhammadaziz.ecommerce.repository;

import com.rifqimuhammadaziz.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}

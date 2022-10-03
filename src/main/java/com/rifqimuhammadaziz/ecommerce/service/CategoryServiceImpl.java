package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Category;
import com.rifqimuhammadaziz.ecommerce.exception.ResourceNotFoundException;
import com.rifqimuhammadaziz.ecommerce.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " is not found"));
    }

    @Override
    public Category create(Category category) {
        category.setId(UUID.randomUUID().toString());
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }
}

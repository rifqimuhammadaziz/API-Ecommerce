package com.rifqimuhammadaziz.ecommerce.controller;

import com.rifqimuhammadaziz.ecommerce.entity.Category;
import com.rifqimuhammadaziz.ecommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public Category findById(@PathVariable String id) {
        return categoryService.findById(id);
    }

    @PostMapping("/categories")
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping("/categories")
    public Category update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteById(@PathVariable String id) {
        categoryService.deleteById(id);
    }

}

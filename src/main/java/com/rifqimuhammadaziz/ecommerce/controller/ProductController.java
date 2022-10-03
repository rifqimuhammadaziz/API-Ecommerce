package com.rifqimuhammadaziz.ecommerce.controller;

import com.rifqimuhammadaziz.ecommerce.entity.Product;
import com.rifqimuhammadaziz.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> products() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable String id) {
        return productService.findById(id);
    }

    @PostMapping("/products")
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product) {
        return productService.update(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteById(@PathVariable String id) {
        productService.deleteById(id);
    }
}

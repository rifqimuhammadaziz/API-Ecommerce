package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Product;
import com.rifqimuhammadaziz.ecommerce.exception.BadRequestException;
import com.rifqimuhammadaziz.ecommerce.exception.ResourceNotFoundException;
import com.rifqimuhammadaziz.ecommerce.repository.CategoryRepository;
import com.rifqimuhammadaziz.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " is not found"));
    }

    @Override
    public Product create(Product product) {
        if (!StringUtils.hasText(product.getName())) {
            throw new BadRequestException("Product Name is required");
        }

        if (product.getCategory() == null) {
            throw new BadRequestException("Category is required");
        }

        if (!StringUtils.hasText(product.getCategory().getId())) {
            throw new BadRequestException("Category ID is required");
        }

        categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Category with ID: " + product.getCategory().getId() + " is not found"));

        product.setId(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        if (!StringUtils.hasText(product.getId())) {
            throw new BadRequestException("Product ID is required");
        }

        if (!StringUtils.hasText(product.getName())) {
            throw new BadRequestException("Product Name is required");
        }

        if (product.getCategory() == null) {
            throw new BadRequestException("Category is required");
        }

        if (!StringUtils.hasText(product.getCategory().getId())) {
            throw new BadRequestException("Category ID is required");
        }

        categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Category with ID: " + product.getCategory().getId() + " is not found"));

        return productRepository.save(product);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product changeImage(String id, String image) {
        Product product = findById(id);
        product.setImage(image);
        return productRepository.save(product);
    }

}

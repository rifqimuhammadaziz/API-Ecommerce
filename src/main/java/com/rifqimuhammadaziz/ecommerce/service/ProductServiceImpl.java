package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.Product;
import com.rifqimuhammadaziz.ecommerce.exception.ResourceNotFoundException;
import com.rifqimuhammadaziz.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
        product.setId(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
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

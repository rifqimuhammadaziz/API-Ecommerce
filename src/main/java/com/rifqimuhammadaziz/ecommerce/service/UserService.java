package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(String id);
    User create(User user);
    User update(User user);
    void deleteById(String id);
}

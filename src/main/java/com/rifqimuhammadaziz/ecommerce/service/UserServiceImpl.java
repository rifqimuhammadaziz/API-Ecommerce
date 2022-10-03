package com.rifqimuhammadaziz.ecommerce.service;

import com.rifqimuhammadaziz.ecommerce.entity.User;
import com.rifqimuhammadaziz.ecommerce.exception.BadRequestException;
import com.rifqimuhammadaziz.ecommerce.exception.ResourceNotFoundException;
import com.rifqimuhammadaziz.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " is not found"));
    }

    @Override
    public User create(User user) {
        if (!StringUtils.hasText(user.getId())) {
            throw new BadRequestException("Username is required");
        }

        if (userRepository.existsById(user.getId())) {
            throw new BadRequestException("Username " + user.getId() + " is registered, please use another username");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email is required");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email " + user.getEmail() + " is registered, please use another email");
        }

        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (!StringUtils.hasText(user.getId())) {
            throw new BadRequestException("Username is required");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email is required");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}

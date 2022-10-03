package com.rifqimuhammadaziz.ecommerce.repository;

import com.rifqimuhammadaziz.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}

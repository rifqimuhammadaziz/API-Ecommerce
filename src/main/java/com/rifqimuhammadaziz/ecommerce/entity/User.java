package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "users")
public class User implements Serializable {

    @Id
    private String id;
    private String password;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
    private String roles;
    private Boolean active;
}

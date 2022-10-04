package com.rifqimuhammadaziz.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    private String id;
    @JsonIgnore
    private String password;
    private String fullName;
    @JsonIgnore
    private String address;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String phoneNumber;
    @JsonIgnore
    private String roles;
    @JsonIgnore
    private Boolean active;

    public User(String username) {
        this.id = username;
    }
}

package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    private String id;
    private String name;

}

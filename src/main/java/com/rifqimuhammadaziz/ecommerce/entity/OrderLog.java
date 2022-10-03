package com.rifqimuhammadaziz.ecommerce.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "orderlogs")
public class OrderLog implements Serializable {

    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Order order;

    @JoinColumn
    @ManyToOne
    private User user;

    private Integer logType;
    private String logMessage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}

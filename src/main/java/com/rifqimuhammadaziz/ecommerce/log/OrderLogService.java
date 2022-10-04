package com.rifqimuhammadaziz.ecommerce.log;

import com.rifqimuhammadaziz.ecommerce.entity.Order;
import com.rifqimuhammadaziz.ecommerce.entity.OrderLog;
import com.rifqimuhammadaziz.ecommerce.entity.User;
import com.rifqimuhammadaziz.ecommerce.repository.OrderLogRepository;
import com.rifqimuhammadaziz.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderLogService {

    @Autowired
    private OrderLogRepository orderLogRepository;

    public final static int DRAFT = 0;
    public final static int PAID = 10;
    public final static int PACKING = 20;
    public final static int DELIVERING = 30;
    public final static int DELIVERED = 40;
    public final static int CANCELLED = 90;

    public void createLog(String username, Order order, int type, String message) {
        OrderLog log = new OrderLog();
        log.setId(UUID.randomUUID().toString());
        log.setLogMessage(message);
        log.setLogType(type);
        log.setOrder(order);
        log.setUser(new User(username));
        log.setDate(new Date());
        orderLogRepository.save(log);
    }
}

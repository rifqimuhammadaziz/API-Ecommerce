package com.rifqimuhammadaziz.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ApiEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEcommerceApplication.class, args);
    }

}

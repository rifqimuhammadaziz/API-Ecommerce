package com.rifqimuhammadaziz.ecommerce.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JWTResponse implements Serializable {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;

    public JWTResponse(String accessToken, String refreshToken, String username, String email) {
        this.username = username;
        this.email = email;
        this.token = accessToken;
        this.refreshToken = refreshToken;
    }
}

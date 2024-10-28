package com.my.jwtDemo.dto;

import com.my.jwtDemo.constant.Role;
import lombok.Getter;

import java.util.List;

@Getter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private Role role;

    public JwtResponse(String accessToken, String username, String email, Role role) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
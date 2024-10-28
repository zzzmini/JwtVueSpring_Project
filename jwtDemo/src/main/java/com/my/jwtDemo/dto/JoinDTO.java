package com.my.jwtDemo.dto;

import com.my.jwtDemo.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinDTO {
    private String username;
    private String password;
    private String email;
    private Role role;
}

package com.my.jwtDemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDto {
    private String username;
    private String role;
    private String email;
}

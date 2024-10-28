package com.my.jwtDemo.controller;

import com.my.jwtDemo.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AdminController {
    @GetMapping("/admin")
    public ResponseEntity<ResponseDto> adminP() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        ResponseDto dto = ResponseDto.builder()
                .email(email)
                .role(role)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dto);
    }
}

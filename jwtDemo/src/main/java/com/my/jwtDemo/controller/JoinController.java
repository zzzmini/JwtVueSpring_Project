package com.my.jwtDemo.controller;

import com.my.jwtDemo.constant.Role;
import com.my.jwtDemo.dto.CustomUserDetails;
import com.my.jwtDemo.dto.JoinDTO;
import com.my.jwtDemo.dto.JwtResponse;
import com.my.jwtDemo.dto.LoginRequest;
import com.my.jwtDemo.jwt.JwtUtil;
import com.my.jwtDemo.service.JoinService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@CrossOrigin(origins = "http://localhost:8080")
public class JoinController {
    private final AuthenticationManager authenticationManager;
    private final JoinService joinService;
    private final JwtUtil jwtUtil;

    public JoinController(JoinService joinService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.joinService = joinService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        System.out.println(joinDTO);
        boolean result = joinService.joinProcess(joinDTO);
        if(result){
            return new ResponseEntity<String>("OK", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
        }
    }
}

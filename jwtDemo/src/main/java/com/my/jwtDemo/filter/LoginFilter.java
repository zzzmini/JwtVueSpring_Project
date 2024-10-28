package com.my.jwtDemo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.jwtDemo.dto.CustomUserDetails;
import com.my.jwtDemo.dto.LoginRequest;
import com.my.jwtDemo.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String usernameParameter = "email";
        return request.getParameter(usernameParameter);
    }

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도 : [LoginFilter:attemptAuthentication]");
        LoginRequest loginRequest = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // userEmail. password를 받는다.
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            System.out.println(loginRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 클라이언트 요청에서 username, password 추출
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        // 스프링 시큐리티에서 username과 password 검증
        // token에 담아서 검증 보냄
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        username, password, null
                );

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {

        // UserDetails 정보가져오기
        CustomUserDetails customUserDetails =
                (CustomUserDetails) authentication.getPrincipal();
        // UserName과 role 꺼내기
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        GrantedAuthority authority = iterator.next();

        String role = authority.getAuthority();
        Long expiredMillSecond = 60 * 60 * 30 * 1000L;
        String token = jwtUtil.createJwt(username, role, expiredMillSecond);

        System.out.println("============ Success");
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        System.out.println("============ Fail");
        response.setStatus(401);
    }
}

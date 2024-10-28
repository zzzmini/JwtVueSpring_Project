package com.my.jwtDemo.config;

import com.my.jwtDemo.filter.LoginFilter;
import com.my.jwtDemo.jwt.JwtFilter;
import com.my.jwtDemo.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    // JwtUtil 주입
    private final JwtUtil jwtUtil;

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtUtil jwtUtil, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/index.html",
                "favicon.ico",
                "/css/**",
                "/js/**",
                "/images/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // cors 처리
        http
            .cors((cors) -> cors
                .configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(
                                Collections.singletonList("http://localhost:8080")
                        );
//                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                        configuration.setAllowCredentials(true);
//                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
//                        configuration.setExposedHeaders(Collections.singletonList("Authorization")
                        configuration.setExposedHeaders(List.of("accessToken","Authorization","Content-Type", "Accept"));
                                configuration.setAllowedHeaders(List.of("accessToken","Authorization","Content-Type", "Accept"));
                        return  configuration;
                    }
                }));

        // csrf disable
        http
            .csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/join").permitAll()
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/user", "/main").hasRole("USER")
                .anyRequest().authenticated());
        // JwtFilter 등록
        http
            .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);
        //필터 추가
        // LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에
        // authenticationConfiguration 객체를 넣어야 함)
        // 따라서 등록 필요
        //AuthenticationManager()와 JWTUtil 인수 전달
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);


        //세션 설정
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

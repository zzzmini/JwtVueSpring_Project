package com.my.jwtDemo.repository;

import com.my.jwtDemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    Boolean existsByUsername(String username);
//    UserEntity findByUsername(String username);
    Boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
}

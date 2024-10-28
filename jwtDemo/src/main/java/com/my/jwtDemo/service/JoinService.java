package com.my.jwtDemo.service;

import com.my.jwtDemo.constant.Role;
import com.my.jwtDemo.dto.JoinDTO;
import com.my.jwtDemo.entity.UserEntity;
import com.my.jwtDemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean joinProcess(JoinDTO joinDTO) {
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();
        String username = joinDTO.getUsername();
        Role role = joinDTO.getRole();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            return false;
        }
        UserEntity data = new UserEntity();

        data.setEmail(email);
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole(role);

        userRepository.save(data);
        return true;
    }
}

package com.example.missiontshoppingmall.service;

import com.example.missiontshoppingmall.entity.UserEntity;
import com.example.missiontshoppingmall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //repo에서 userId로 찾아오기
        Optional<UserEntity> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = optionalUser.get();

        //리턴을 UserDetails 형태로 한다
        UserDetails userDetails = User.withUsername(userEntity.getUserId())
                .password(userEntity.getPassword())
                .build();
        return userDetails;
    }


    @Override
    public void createUser(UserDetails user) {
        // userId가 겹치면 안됨
        if (this.userExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {//안겹치면 가입진행
            UserDetails userDetails = user;
            UserEntity newUser = UserEntity.builder()
                    .userId(userDetails.getUsername())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .authority(userDetails.getAuthorities().toString())
                    .build();
            userRepository.save(newUser);
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);


    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);

    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsByUserId(userId);
    }


}

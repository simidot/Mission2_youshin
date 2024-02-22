package com.example.missiontshoppingmall.service;

import com.example.missiontshoppingmall.dto.UserLoginDto;
import com.example.missiontshoppingmall.dto.UserRegisterDto;
import com.example.missiontshoppingmall.jwt.JwtResponseDto;
import com.example.missiontshoppingmall.jwt.JwtTokenUtils;
import com.example.missiontshoppingmall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseDto userLogin(UserLoginDto dto) {
        if (!manager.userExists(dto.getUserId())) {
            log.warn("user not exists");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = manager.loadUserByUsername(dto.getUserId());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            log.warn("password mismatch");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //일치하면 토큰 발급
        JwtResponseDto jwtResponseDto = JwtResponseDto.builder()
                .token(jwtTokenUtils.generateToken(userDetails)).build();
        log.info(jwtResponseDto.getToken());
        return jwtResponseDto;
    }

    public void userRegister(UserRegisterDto dto) {
        if (manager.userExists(dto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_INACTIVE"));
            UserDetails userDetails = new User(dto.getUserId(), dto.getPassword(), authorityList);
            log.info(userDetails.toString());
            manager.createUser(userDetails);
        }
    }

}

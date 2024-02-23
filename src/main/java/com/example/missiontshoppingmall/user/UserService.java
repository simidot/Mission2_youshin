package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.user.dto.BARequest;
import com.example.missiontshoppingmall.user.dto.UserAdditionalInfoDto;
import com.example.missiontshoppingmall.user.dto.UserRegisterDto;
import com.example.missiontshoppingmall.user.dto.client.BAResponse;
import com.example.missiontshoppingmall.user.dto.client.UserAdditionalInfoResponse;
import com.example.missiontshoppingmall.user.dto.client.UserRegisterResponse;
import com.example.missiontshoppingmall.user.entity.CustomUserDetails;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.jwt.JwtRequestDto;
import com.example.missiontshoppingmall.user.jwt.JwtResponseDto;
import com.example.missiontshoppingmall.user.jwt.JwtTokenUtils;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseDto userLogin(JwtRequestDto dto) {
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

    public UserRegisterResponse userRegister(UserRegisterDto dto) { //dto에서 UserDetails로 변환
        CustomUserDetails inactiveUser = CustomUserDetails.builder()
                .userId(dto.getUserId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authorities("ROLE_INACTIVE")
                .build();

        log.info(inactiveUser.toString());
        manager.createUser(inactiveUser);
        UserRegisterResponse response = UserRegisterResponse.builder().userId(inactiveUser.getUserId()).build();
        return response;
    }

    @Transactional
    public UserAdditionalInfoResponse additionalInfo(String accountId, UserAdditionalInfoDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();

        boolean dtoCheck = Stream.of(
                        dto.getAge(), dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPhone())
                .allMatch(Objects::nonNull);
        if (dtoCheck) {
            foundUser.setUsername(dto.getUsername());
            foundUser.setEmail(dto.getEmail());
            foundUser.setNickname(dto.getNickname());
            foundUser.setPhone(dto.getPhone());
            foundUser.setAge(dto.getAge());
            foundUser.setAuthority("ROLE_ACTIVE");

            foundUser = userRepository.save(foundUser);
        }
        UserAdditionalInfoResponse response = UserAdditionalInfoResponse.fromEntity(foundUser);
        return response;
    }

    @Transactional
    public BAResponse registerBusinessAccount(String accountId, BARequest dto) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity foundUser = optionalUser.get();
        foundUser.setBusinessNumber(dto.getBusinessNumber());
        userRepository.save(foundUser);
        BAResponse response = BAResponse.builder()
                .accountId(accountId)
                .businessNumber(dto.getBusinessNumber())
                .businessIsAllowed(false)
                .authority("ROLE_ACTIVE") //수락 이전이므로 ACTIVE인 상태
                .build();
        return response;
    }

}

package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.user.dto.request.BARequest;
import com.example.missiontshoppingmall.user.dto.request.UserAdditionalInfoDto;
import com.example.missiontshoppingmall.user.dto.request.UserRegisterDto;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import com.example.missiontshoppingmall.user.dto.response.AdditionalInfo;
import com.example.missiontshoppingmall.user.dto.response.UserRegisterResponse;
import com.example.missiontshoppingmall.user.entity.CustomUserDetails;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.jwt.JwtRequestDto;
import com.example.missiontshoppingmall.user.jwt.JwtResponseDto;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final CustomUserDetailsManager manager;

    public JwtResponseDto userLogin(JwtRequestDto dto) {
        return manager.userLogin(dto);
    }

    @Transactional
    public UserRegisterResponse userRegister(UserRegisterDto dto) {
        //dto에서 UserDetails로 변환
        CustomUserDetails inactiveUser = CustomUserDetails.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .authorities("ROLE_INACTIVE")
                .build();

        log.info(inactiveUser.toString());
        manager.createUser(inactiveUser);
        return UserRegisterResponse.builder()
                .userId(inactiveUser.getUserId())
                .build();
    }

    // 추가 정보 입력 (인증관련 정보가 아니므로 그냥 UserService에서 메서드 구현하였다.)
    @Transactional
    public AdditionalInfo additionalInfo(String accountId, UserAdditionalInfoDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();

        // 하나라도 null이면 false 반환, 모두 null이 아니면 true 반환
        boolean dtoCheck = Stream.of(
                        dto.getAge(), dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPhone())
                .allMatch(Objects::nonNull);
        if (dtoCheck) { // true면 정보 업데이트 + ACTIVE USER로 권한설정
            foundUser.setUsername(dto.getUsername());
            foundUser.setEmail(dto.getEmail());
            foundUser.setNickname(dto.getNickname());
            foundUser.setPhone(dto.getPhone());
            foundUser.setAge(dto.getAge());
            foundUser.setAuthority("ROLE_ACTIVE");
            foundUser = userRepository.save(foundUser);
        }
        // true면 업데이트된 정보를, 아닐경우는 본래정보 리턴
        AdditionalInfo response = AdditionalInfo.fromEntity(foundUser);
        return response;
    }

    // 사업자계정 등록 요청
    @Transactional
    public BAResponse registerBA (String accountId, BARequest dto) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();
        foundUser.setBusinessNumber(dto.getBusinessNumber());
        foundUser.setBusinessIsAllowed(false);
        foundUser = userRepository.save(foundUser);

        return BAResponse.fromEntity(foundUser);
    }

    //사업자계정 등록결과 확인
    public BAResponse baResultCheck(String accountId) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();
        return BAResponse.fromEntity(foundUser);
    }

}

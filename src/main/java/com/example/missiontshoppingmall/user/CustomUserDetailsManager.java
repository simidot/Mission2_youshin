package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.utils.AuthenticationFacade;
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
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //repo에서 userId로 찾아오기
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(userId);
        if (optionalUser.isEmpty()) {
            log.info("loadUserByUsername : not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = optionalUser.get();
        //리턴을 UserDetails 형태로 한다
        UserDetails userDetails = CustomUserDetails.builder()
                .userId(userEntity.getAccountId())
                .password(userEntity.getPassword())
                .authorities(userEntity.getAuthority())
                .build();
        return userDetails;
    }

    public JwtResponseDto userLogin(JwtRequestDto dto) {
        // 넘어온 dto의 계정id가 없는 계정이라면 예외 발생
        UserDetails userDetails = this.loadUserByUsername(dto.getUserId());
        // 비밀번호가 옳지 않아도 예외 발생
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            log.warn("password mismatch");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //일치하면 토큰 발급
        String generatedToken = jwtTokenUtils.generateToken(userDetails);
        // 토큰과 subject dto에 넣어 반환
        JwtResponseDto jwtResponseDto = JwtResponseDto.builder()
                .token(generatedToken)
                .subject(jwtTokenUtils.parseClaims(generatedToken).getSubject())
                .build();
        log.info("토큰 발급 완료 : {}", jwtResponseDto.getSubject());
        return jwtResponseDto;
    }


    @Override
    public void createUser(UserDetails user) {
        // userId가 겹치면 안됨
        if (this.userExists(user.getUsername())) {
            log.info("user already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {//안겹치면 가입진행
            CustomUserDetails userDetails = (CustomUserDetails) user;

            UserEntity newUser = UserEntity.builder()
                    .accountId(userDetails.getUserId())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .authority(userDetails.getStringAuthorities())
                    .build();
            userRepository.save(newUser);
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsByAccountId(userId);
    }


    // 로그인한 아이디와 해당 아이디가 같은지 확인하는 메서드
    public void checkIdIsEqual(String accountId) {
        String loginId = facade.getAuth().getName(); //현재 인증정보의 id name
        // 다르면 예외처리
        if (!accountId.equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 같으면 계속 이어서 감
    }

    // 로그인한 아이디와 해당 아이디가 같은지 확인하는 메서드
    public void checkIdIsNotEqual(String accountId) {
        String loginId = facade.getAuth().getName(); //현재 인증정보의 id name
        // 다르면 예외처리
        if (accountId.equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 같으면 계속 이어서 감
    }

    // 인증정보에서 UserEntity를 추출하는 메서드
    public UserEntity loadUserFromAuth() {
        //repo에서 userId로 찾아오기
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(facade.getAuth().getName());
        if (optionalUser.isEmpty()) {
            log.info("loadUserFromAuth : not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalUser.get();
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
}

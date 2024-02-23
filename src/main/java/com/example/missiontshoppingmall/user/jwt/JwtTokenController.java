package com.example.missiontshoppingmall.user.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class JwtTokenController {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/issue")
    public JwtResponseDto issueJwt(
            @RequestBody JwtRequestDto dto
    ) {
        if (!manager.userExists(dto.getUserId())) { //아이디가 없는 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // id로 유저정보 찾아오기
        UserDetails userDetails =
                manager.loadUserByUsername(dto.getUserId());

        // 비밀번호가 다른 경우
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 일치하면
        JwtResponseDto response = JwtResponseDto.builder()
                .token(jwtTokenUtils.generateToken(userDetails)).build();
        return response;
    }

//    @GetMapping("/validate")

}

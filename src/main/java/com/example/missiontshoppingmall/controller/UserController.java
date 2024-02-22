package com.example.missiontshoppingmall.controller;

import com.example.missiontshoppingmall.dto.UserLoginDto;
import com.example.missiontshoppingmall.dto.UserRegisterDto;
import com.example.missiontshoppingmall.jwt.JwtResponseDto;
import com.example.missiontshoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public String userLogin(
            @RequestBody UserLoginDto dto
    ) {
        log.info("login 시도!");
        JwtResponseDto jwtResponseDto = userService.userLogin(dto);
        log.info("login 성공: token {}", jwtResponseDto.getToken());
        return "로그인 성공";
    }

    @PostMapping("/register")
    public String register(
            @RequestBody UserRegisterDto dto
    ) {
        log.info("회원가입 시도!!");
        userService.userRegister(dto);
        return "회원가입 성공";
    }
}

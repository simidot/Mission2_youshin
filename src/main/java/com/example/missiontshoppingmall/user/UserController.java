package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.user.dto.request.BARequest;
import com.example.missiontshoppingmall.user.dto.request.UserAdditionalInfoDto;
import com.example.missiontshoppingmall.user.dto.request.UserRegisterDto;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import com.example.missiontshoppingmall.user.dto.response.AdditionalInfo;
import com.example.missiontshoppingmall.user.dto.response.UserRegisterResponse;
import com.example.missiontshoppingmall.user.jwt.JwtRequestDto;
import com.example.missiontshoppingmall.user.jwt.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public JwtResponseDto userLogin(
            @RequestBody JwtRequestDto dto
    ) {
        log.info("login 시도!");
        JwtResponseDto jwtResponseDto = userService.userLogin(dto);
        return jwtResponseDto; //로그인 이후 클라이언트 측에서 반환된 토큰을 헤더에 포함시켜 서버로 보낸다.
    }

    // 회원가입
    @PostMapping("/register")
    public UserRegisterResponse register(
            @RequestBody UserRegisterDto dto
    ) {
        log.info("회원가입 시도!!");
        return userService.userRegister(dto);
    }

    // 회원 추가정보 입력 (입력 시 ACTIVE USER로 자동변환)
    @PostMapping("/{accountId}/additional-info")
    public AdditionalInfo registerAdditionalInfo(
            @PathVariable("accountId") String id,
            @RequestBody UserAdditionalInfoDto dto
    ) {
        return userService.additionalInfo(id, dto);
    }

    // 비즈니스 계정으로 전환 신청
    @PostMapping("/{accountId}/business-request")
    public BAResponse baRequest(
            @PathVariable("accountId") String accountId,
            @RequestBody BARequest requestDto
    ) {
        return userService.registerBA(accountId, requestDto);
    }

    // 비즈니스 계정으로 전환 결과 확인
    @GetMapping("/{accountId}/business-request")
    public BAResponse baResultCheck(
            @PathVariable("accountId") String accountId
    ) {
        return userService.baResultCheck(accountId);
    }
}


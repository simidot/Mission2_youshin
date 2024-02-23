package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.user.dto.BARequest;
import com.example.missiontshoppingmall.user.dto.UserAdditionalInfoDto;
import com.example.missiontshoppingmall.user.dto.UserRegisterDto;
import com.example.missiontshoppingmall.user.dto.client.BAResponse;
import com.example.missiontshoppingmall.user.dto.client.UserAdditionalInfoResponse;
import com.example.missiontshoppingmall.user.dto.client.UserRegisterResponse;
import com.example.missiontshoppingmall.user.jwt.JwtRequestDto;
import com.example.missiontshoppingmall.user.jwt.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public JwtResponseDto userLogin(
            @RequestBody JwtRequestDto dto
    ) {
        log.info("login 시도!");
        JwtResponseDto jwtResponseDto = userService.userLogin(dto);
        log.info("login 성공: token {}", jwtResponseDto.getToken());
        return jwtResponseDto; //로그인 이후 클라이언트 측에서 토큰을 헤더에 포함시켜 서버로 보내게 된다.
    }

    @PostMapping("/register")
    public UserRegisterResponse register(
            @RequestBody UserRegisterDto dto
    ) {
        log.info("회원가입 시도!!");
        return userService.userRegister(dto);
    }

    @PostMapping("/{accountId}/additional-info")
    public UserAdditionalInfoResponse registerAdditionalInfo(
            Authentication authentication,
            @PathVariable("accountId") String id,
            @RequestBody UserAdditionalInfoDto dto
    ) {
        // 클라이언트측에서 추가정보에 대한 validation이 진행되었다고 가정.
        return userService.additionalInfo(id, dto);
    }

    @PostMapping("/{accountId}/business-request")
    public BAResponse businessAccountRequest(
            @PathVariable("accountId") String accountId,
            @RequestBody BARequest requestDto
    ) {
        return userService.registerBusinessAccount(accountId, requestDto);
    }
}


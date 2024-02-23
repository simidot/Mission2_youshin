package com.example.missiontshoppingmall.user.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto { //로그인 요청 후 반환 DTO 토큰값 반환
    private String subject;
    private String token;
}

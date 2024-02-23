package com.example.missiontshoppingmall.user.jwt;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtRequestDto { //로그인시 JwtToken받기위한 요청Dto
    private String userId;
    private String password;
}

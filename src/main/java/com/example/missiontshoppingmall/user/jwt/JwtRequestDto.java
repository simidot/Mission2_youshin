package com.example.missiontshoppingmall.user.jwt;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtRequestDto {
    private String userId;
    private String password;
}

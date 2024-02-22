package com.example.missiontshoppingmall.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDto {
    private String userId;
    private String password;
}

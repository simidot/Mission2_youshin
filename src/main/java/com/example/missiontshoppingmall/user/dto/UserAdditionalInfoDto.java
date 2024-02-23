package com.example.missiontshoppingmall.user.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAdditionalInfoDto {
    private String username;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;
}

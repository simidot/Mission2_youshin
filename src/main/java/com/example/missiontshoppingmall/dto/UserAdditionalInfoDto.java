package com.example.missiontshoppingmall.dto;

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

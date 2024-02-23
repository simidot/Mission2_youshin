package com.example.missiontshoppingmall.user.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegisterDto {
    //필수
    private String userId;
    private String password;

    //추가 입력 정보
//    private UserAdditionalInfoDto additionalInfo;

}

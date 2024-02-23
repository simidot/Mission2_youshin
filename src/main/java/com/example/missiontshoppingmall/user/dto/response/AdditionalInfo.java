package com.example.missiontshoppingmall.user.dto.response;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdditionalInfo { //추가정보+계정id+권한
    private String accountId;
    private String authority;
    private String username;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;

    public static AdditionalInfo fromEntity(UserEntity entity) {
        return AdditionalInfo.builder()
                .accountId(entity.getAccountId())
                .authority(entity.getAuthority())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }
}

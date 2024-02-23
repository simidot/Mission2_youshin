package com.example.missiontshoppingmall.user.dto.client;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAdditionalInfoResponse {
    private String accountId;
    private String authority;
    private String username;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;

    public static UserAdditionalInfoResponse fromEntity(UserEntity entity) {
        return UserAdditionalInfoResponse.builder()
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

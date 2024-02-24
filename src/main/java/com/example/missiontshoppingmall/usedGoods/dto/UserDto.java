package com.example.missiontshoppingmall.usedGoods.dto;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String nickname;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .userId(entity.getAccountId())
                .nickname(entity.getNickname())
                .build();
    }
}

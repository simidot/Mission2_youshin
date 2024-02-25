package com.example.missiontshoppingmall.usedGoods.dto;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String accountId;
    private String nickname;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .accountId(entity.getAccountId())
                .nickname(entity.getNickname())
                .build();
    }
}

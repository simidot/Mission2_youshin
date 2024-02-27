package com.example.missiontshoppingmall.user.dto.response;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAResult {
    private Long id;
    private String accountId;
    private String authority;
    private String businessNumber;
    private boolean businessIsAllowed;
    private Long shoppingMallId;

    public static BAResult fromEntity(UserEntity entity) {
        return BAResult.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .authority(entity.getAuthority())
                .businessIsAllowed(entity.getBusinessIsAllowed())
                .businessNumber(entity.getBusinessNumber())
                .build();
    }

}

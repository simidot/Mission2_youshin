package com.example.missiontshoppingmall.user.dto.response;

import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAResponse {
    private Long id;
    private String accountId;
    private String authority;
    private String businessNumber;
    private boolean businessIsAllowed;

    public static BAResponse fromEntity(UserEntity entity) {
        return BAResponse.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .authority(entity.getAuthority())
                .businessIsAllowed(entity.getBusinessIsAllowed())
                .businessNumber(entity.getBusinessNumber())
                .build();
    }
}

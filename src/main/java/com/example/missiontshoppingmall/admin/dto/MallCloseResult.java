package com.example.missiontshoppingmall.admin.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MallCloseResult {
    private String owner;
    private String name;
    private String closeReason;
    private RunningStatus runningStatus;
    private boolean closeIsAllowed;
    private String deniedReason;

    public static MallCloseResult fromEntity(ShoppingMall entity) {
        return MallCloseResult.builder()
                .owner(entity.getOwner().getAccountId())
                .name(entity.getName())
                .closeIsAllowed(entity.getCloseIsAllowed())
                .closeReason(entity.getCloseReason())
                .deniedReason(entity.getDeniedReason())
                .runningStatus(entity.getRunningStatus())
                .build();
    }
}

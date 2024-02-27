package com.example.missiontshoppingmall.admin.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.Allowance;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallCloseResult {
    private String owner;
    private String name;
    private String closeReason;
    private RunningStatus runningStatus;
    private Allowance allowance;
    private String deniedReason;

    public static MallCloseResult fromEntity(ShoppingMall entity) {
        return MallCloseResult.builder()
                .owner(entity.getOwner().getAccountId())
                .name(entity.getName())
                .allowance(entity.getAllowance())
                .closeReason(entity.getCloseReason())
                .deniedReason(entity.getDeniedReason())
                .runningStatus(entity.getRunningStatus())
                .build();
    }
}

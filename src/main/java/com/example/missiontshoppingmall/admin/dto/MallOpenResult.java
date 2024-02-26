package com.example.missiontshoppingmall.admin.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MallOpenResult {
    private String owner;
    private String name;
    private boolean openIsAllowed;
    private String deniedReason;

    public static MallOpenResult fromEntity(ShoppingMall entity) {
        return MallOpenResult.builder()
                .owner(entity.getOwner().getAccountId())
                .name(entity.getName())
                .openIsAllowed(entity.getOpenIsAllowed())
                .deniedReason(entity.getDeniedReason())
                .build();
    }
}

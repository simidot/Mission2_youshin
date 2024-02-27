package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MallCloseResponse {
    private String owner;
    private String name;
    private RequestType requestType;
    private String closeReason;

    public static MallCloseResponse fromEntity(ShoppingMall entity) {
        return MallCloseResponse.builder()
                .owner(entity.getOwner().getAccountId())
                .name(entity.getName())
                .requestType(entity.getRequestType())
                .closeReason(entity.getCloseReason())
                .build();
    }
}

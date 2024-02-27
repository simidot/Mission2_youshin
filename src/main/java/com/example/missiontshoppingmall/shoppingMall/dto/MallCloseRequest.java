package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallCloseRequest {
    private RequestType requestType;
    private String closeReason;
}

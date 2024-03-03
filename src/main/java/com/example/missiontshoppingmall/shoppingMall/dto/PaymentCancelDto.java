package com.example.missiontshoppingmall.shoppingMall.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentCancelDto {
    private String cancelReason; // 취소 사유
}

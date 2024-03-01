package com.example.missiontshoppingmall.utils;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentCancelDto {
    private String cancelReason; // 취소 사유
}

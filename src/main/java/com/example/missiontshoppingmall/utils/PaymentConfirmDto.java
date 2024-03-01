package com.example.missiontshoppingmall.utils;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentConfirmDto {
    private String paymentKey; //결제의 키값
    private String orderId; // 주문id
    private Integer amount; // 결제 금액
    private String bearerToken; //bearerToken을 통해 어떤 사용자가 결제를 요청하는지 확인
}

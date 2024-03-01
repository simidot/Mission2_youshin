package com.example.missiontshoppingmall.order.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderConfirmDto {
    private boolean confirmation;
    private String deniedReason;
}

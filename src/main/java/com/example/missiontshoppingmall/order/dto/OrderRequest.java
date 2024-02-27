package com.example.missiontshoppingmall.order.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequest {
    private Integer amount;
}

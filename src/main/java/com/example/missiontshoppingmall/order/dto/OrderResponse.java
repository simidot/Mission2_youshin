package com.example.missiontshoppingmall.order.dto;

import com.example.missiontshoppingmall.item.dto.ItemDto;
import com.example.missiontshoppingmall.item.dto.ItemInfoWithoutMall;
import com.example.missiontshoppingmall.order.entity.Order;
import com.example.missiontshoppingmall.order.entity.PaymentStatus;
import com.example.missiontshoppingmall.order.entity.TransactionStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponse {
    private Integer amount;
    private PaymentStatus paymentStatus;
    private TransactionStatus transactionStatus;
    private String buyerId;
    private ItemInfoWithoutMall item;
    private Integer totalPrice;

    public static OrderResponse fromEntity(Order entity) {
        return OrderResponse.builder()
                .amount(entity.getAmount())
                .paymentStatus(entity.getPaymentStatus())
                .transactionStatus(entity.getTransactionStatus())
                .buyerId(entity.getBuyer().getAccountId())
                .item(ItemInfoWithoutMall.fromEntity(entity.getOrderItem()))
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}

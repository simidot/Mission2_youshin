package com.example.missiontshoppingmall.order.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemOrder extends BaseEntity {
    @Setter
    private Integer amount; //주문수량

    @Setter
    private Integer totalPrice; //총 주문금액
    
    // 결제 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    // 구매요청 수락 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item orderItem;
}

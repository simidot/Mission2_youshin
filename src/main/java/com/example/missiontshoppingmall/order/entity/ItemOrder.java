package com.example.missiontshoppingmall.order.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    // 결제 키
    @Setter
    private String paymentKey;

    // 구매요청 수락 상태
    @Setter
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Setter
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item orderItem;
}

package com.example.missiontshoppingmall.shoppingMall.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShoppingMall extends BaseEntity {
    @Setter
    private String name;
    @Lob
    @Setter
    private String description;

    // 최근 거래일자 (주문이 완료되면 계속 update)
    @Setter
    private LocalDateTime recentOrderDate;

    //쇼핑몰 카테고리 (대분류)
    @Setter
    @Enumerated(EnumType.STRING)
    private LargeCategory largeCategory;

    //쇼핑몰 운영상태
    @Setter
    @Enumerated(EnumType.STRING)
    private RunningStatus runningStatus;
    //요청종류
    @Setter
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    // 요청 허가여부
    @Setter
    @Enumerated(EnumType.STRING)
    private Allowance allowance;
    // 요청 불허이유
    @Setter
    private String deniedReason;
    //폐쇄요청 이유
    @Setter
    private String closeReason;

    @ManyToOne
    private UserEntity owner;

    @OneToMany(mappedBy = "shoppingMall", fetch = FetchType.LAZY)
    private List<Item> itemList;

    public ShoppingMall(RunningStatus runningStatus, UserEntity owner, Allowance allowance) {
        this.runningStatus = runningStatus;
        this.owner = owner;
        this.allowance = allowance;
    }
}

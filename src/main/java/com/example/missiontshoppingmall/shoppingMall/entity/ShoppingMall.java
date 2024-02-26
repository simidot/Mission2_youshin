package com.example.missiontshoppingmall.shoppingMall.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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

    //쇼핑몰 카테고리 (대분류)
    @Setter
    private LargeCategory largeCategory;

    //쇼핑몰 운영상태
    @Setter
    @Enumerated(EnumType.STRING)
    private RunningStatus runningStatus;
    //요청종류
    @Setter
    @Enumerated(EnumType.STRING)
    private RequestType requestType;
    //개설요청 허가여부
    @Setter
    private Boolean openIsAllowed;
    //개설요청 불허이유
    @Setter
    private String deniedReason;
    //폐쇄요청 이유
    @Setter
    private String closeReason;
    //폐쇄요청 허가여부
    @Setter
    private Boolean closeIsAllowed;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @OneToMany(mappedBy = "shoppingMall")
    private List<Item> itemList;
}

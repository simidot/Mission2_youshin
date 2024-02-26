package com.example.missiontshoppingmall.shoppingMall.entity;

import com.example.missiontshoppingmall.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Item extends BaseEntity {
    private String name; //상품이름
    private String image; //상품 이미지 url
    @Lob
    private String description;
    //상품 중분류
    private String mediumCategory;
    //상품 소분류
    private String smallCategory;
    //재고
    private Integer stock;
    //가격
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShoppingMall shoppingMall;

    @OneToMany(mappedBy = "orderItem")
    private List<Order> orderList;
}

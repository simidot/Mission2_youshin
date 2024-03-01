package com.example.missiontshoppingmall.item.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Item extends BaseEntity {
    @Setter
    private String name; //상품이름
    @Lob
    @Setter
    private String description;
    //상품 중분류
    @Setter
    private String mediumCategory;
    //상품 소분류
    @Setter
    private String smallCategory;
    //재고
    @Setter
    private Integer stock;
    //가격
    @Setter
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShoppingMall shoppingMall;

    @OneToMany(mappedBy = "orderItem")
    private List<ItemOrder> orderList;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL,
    fetch = FetchType.LAZY)
    private List<ItemImage> imageList;

    // 연관관계 편의 메서드
    public void addImage(ItemImage image) {
        imageList.add(image);
        image.setItem(this);
    }
}

package com.example.missiontshoppingmall.item.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemImage extends BaseEntity {
    @Setter
    private String imgUrl;

    @ManyToOne
    @Setter
    private Item item;

    // 연관관계 편의 메서드
    public void addItem(Item itemEntity) {
        this.setItem(itemEntity);
        itemEntity.getImageList().add(this);
    }
}

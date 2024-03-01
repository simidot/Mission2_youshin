package com.example.missiontshoppingmall.item.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import jakarta.persistence.Entity;
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

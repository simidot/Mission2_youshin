package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UsedGoodsImage extends BaseEntity {
    @Setter
    private String imgUrl;

    @ManyToOne
    @Setter
    private UsedGoods usedGoods;

    // 연관관계 편의 메서드
    public void addUsedGoods(UsedGoods ug) {
        this.setUsedGoods(ug);
        ug.getImageList().add(this);
    }
}

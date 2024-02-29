package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.BaseEntity;
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

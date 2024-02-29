package com.example.missiontshoppingmall.usedGoods.dto;

import com.example.missiontshoppingmall.item.dto.ItemImageDto;
import com.example.missiontshoppingmall.item.entity.ItemImage;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoodsImage;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsedGoodsImageDto {
    private String imgUrl;

    public static UsedGoodsImageDto fromEntity(UsedGoodsImage entity) {
        return UsedGoodsImageDto.builder()
                .imgUrl(entity.getImgUrl())
                .build();
    }
}


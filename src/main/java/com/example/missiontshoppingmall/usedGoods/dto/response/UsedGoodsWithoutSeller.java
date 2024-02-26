package com.example.missiontshoppingmall.usedGoods.dto.response;

import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsedGoodsWithoutSeller {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minimumPrice;
    private SaleStatus saleStatus;

    public static UsedGoodsWithoutSeller fromEntity(UsedGoods entity) {

        return UsedGoodsWithoutSeller.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .imageUrl(entity.getImageUrl())
                .saleStatus(entity.getSaleStatus())
                .build();
    }

}

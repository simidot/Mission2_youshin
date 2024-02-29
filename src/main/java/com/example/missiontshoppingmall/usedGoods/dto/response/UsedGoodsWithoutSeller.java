package com.example.missiontshoppingmall.usedGoods.dto.response;

import com.example.missiontshoppingmall.item.dto.ItemImageDto;
import com.example.missiontshoppingmall.usedGoods.dto.UsedGoodsImageDto;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoodsImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsedGoodsWithoutSeller {
    private Long id;
    private String title;
    private String description;
    private Integer minimumPrice;
    private SaleStatus saleStatus;
    private List<UsedGoodsImageDto> imageUrls;

    public static UsedGoodsWithoutSeller fromEntity(UsedGoods entity) {

        List<UsedGoodsImageDto> dtoList = entity.getImageList().stream()
                .map(UsedGoodsImageDto::fromEntity)
                .collect(Collectors.toList());

        return UsedGoodsWithoutSeller.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .imageUrls(dtoList)
                .saleStatus(entity.getSaleStatus())
                .build();
    }

}

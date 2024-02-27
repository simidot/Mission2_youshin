package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.shoppingMall.dto.MallInfoWithoutItem;
import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInfoDto {
    private Long id;
    private String name;
    private String image;
    private String description;
    private String mediumCategory;
    private String smallCategory;
    private Integer price;
    private MallInfoWithoutItem shoppingMall;

    public static ItemInfoDto fromEntity(Item entity) {
        return ItemInfoDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .description(entity.getDescription())
                .mediumCategory(entity.getMediumCategory())
                .smallCategory(entity.getSmallCategory())
                .price(entity.getPrice())
                .shoppingMall(MallInfoWithoutItem.fromEntity(entity.getShoppingMall()))
                .build();
    }
}
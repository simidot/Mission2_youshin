package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import lombok.*;

import java.util.stream.Collectors;

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
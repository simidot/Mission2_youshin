package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {
    private String name;
    private String description;
    private String mediumCategory;
    private String smallCategory;
    private Integer price;

    public static ItemDto fromEntity(Item entity) {
        return ItemDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .mediumCategory(entity.getMediumCategory())
                .smallCategory(entity.getSmallCategory())
                .price(entity.getPrice())
                .build();
    }
}

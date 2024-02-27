package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInfoWithoutMall {
    private Long id;
    private String name;
    private String description;
    private Integer price;

    public static ItemInfoWithoutMall fromEntity(Item entity) {
        return ItemInfoWithoutMall.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }
}
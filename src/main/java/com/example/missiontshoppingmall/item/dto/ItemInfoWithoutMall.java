package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInfoWithoutMall {
    private Long id;
    private String name;
    private Integer price;

    public static ItemInfoWithoutMall fromEntity(Item entity) {
        return ItemInfoWithoutMall.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }
}
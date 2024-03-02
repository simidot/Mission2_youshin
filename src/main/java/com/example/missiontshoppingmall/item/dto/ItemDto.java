package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {
    private String name;
    private String description;
    private Integer price;

    public static ItemDto fromEntity(Item entity) {
        return ItemDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }
}

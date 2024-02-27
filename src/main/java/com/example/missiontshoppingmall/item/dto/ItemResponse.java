package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemResponse {
    private Long id;
    private String name;
    private String image;
    private String description;
    private String mediumCategory;
    private String smallCategory;
    private Integer stock;
    private Integer price;

    public static ItemResponse fromEntity(Item entity) {
        return ItemResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .mediumCategory(entity.getMediumCategory())
                .smallCategory(entity.getSmallCategory())
                .stock(entity.getStock())
                .price(entity.getPrice())
                .build();
    }
}

package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private String mediumCategory;
    private String smallCategory;
    private Integer stock;
    private Integer price;
    private List<ItemImageDto> imageUrls;

    public static ItemResponse fromEntity(Item entity) {
        List<ItemImageDto> dtoList = entity.getImageList().stream()
                .map(ItemImageDto::fromEntity)
                .collect(Collectors.toList());

        return ItemResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrls(dtoList)
                .mediumCategory(entity.getMediumCategory())
                .smallCategory(entity.getSmallCategory())
                .stock(entity.getStock())
                .price(entity.getPrice())
                .build();
    }
}

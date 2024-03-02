package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.ItemImage;
import com.example.missiontshoppingmall.shoppingMall.dto.MallInfoWithoutItem;
import com.example.missiontshoppingmall.item.entity.Item;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemInfoDto {
    private Long id;
    private String name;
    private List<ItemImageDto> imageUrls;
    private String description;
    private Integer price;
    private MallInfoWithoutItem shoppingMall;

    public static ItemInfoDto fromEntity(Item entity) {
        List<ItemImageDto> dtoList = entity.getImageList().stream()
                .map(ItemImageDto::fromEntity)
                .collect(Collectors.toList());

        return ItemInfoDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imageUrls(dtoList)
                .description(entity.getDescription())
                .price(entity.getPrice())
                .shoppingMall(MallInfoWithoutItem.fromEntity(entity.getShoppingMall()))
                .build();
    }
}
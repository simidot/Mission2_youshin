package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.item.dto.ItemInfoWithoutMall;
import com.example.missiontshoppingmall.shoppingMall.entity.LargeCategory;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallInfoDto {
    private Long id;
    private String name;
    private String description;
    private LargeCategory largeCategory;
    private List<ItemInfoWithoutMall> itemList;

    public static MallInfoDto fromEntity(ShoppingMall entity) {
        return MallInfoDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .largeCategory(entity.getLargeCategory())
                .itemList(entity.getItemList().stream()
                        .map(ItemInfoWithoutMall::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

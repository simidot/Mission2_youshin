package com.example.missiontshoppingmall.shoppingMall.dto;

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
public class MallInfoWithoutItem {
    private Long id;
    private String name;
    private String description;
    private LargeCategory largeCategory;

    public static MallInfoWithoutItem fromEntity(ShoppingMall entity) {
        return MallInfoWithoutItem.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .largeCategory(entity.getLargeCategory())
                .build();
    }
}

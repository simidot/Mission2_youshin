package com.example.missiontshoppingmall.item.dto;

import com.example.missiontshoppingmall.item.entity.ItemImage;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemImageDto {
    private String imgUrl;

    public static ItemImageDto fromEntity(ItemImage entity) {
        return ItemImageDto.builder()
                .imgUrl(entity.getImgUrl())
                .build();
    }
}

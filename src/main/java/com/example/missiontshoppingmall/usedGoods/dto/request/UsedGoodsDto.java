package com.example.missiontshoppingmall.usedGoods.dto.request;

import com.example.missiontshoppingmall.usedGoods.dto.UsedGoodsImageDto;
import com.example.missiontshoppingmall.usedGoods.dto.UserDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsedGoodsDto {
    private String title;
    private String description;
    private Integer minimumPrice;
    private SaleStatus saleStatus;

    private UserDto seller; //seller
    private List<SuggestionResponse> suggestionList;
    private List<UsedGoodsImageDto> imageUrls;


    public static UsedGoodsDto fromEntity(UsedGoods entity) {
        List<UsedGoodsImageDto> dtoList = new ArrayList<>();
        dtoList = entity.getImageList().stream()
                .map(UsedGoodsImageDto::fromEntity)
                .collect(Collectors.toList());

        return UsedGoodsDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .imageUrls(dtoList)
                .saleStatus(entity.getSaleStatus())
                .seller(UserDto.fromEntity(entity.getSeller()))
                .build();
    }

}


package com.example.missiontshoppingmall.usedGoods.dto.request;

import com.example.missiontshoppingmall.usedGoods.dto.UserDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsedGoodsDto {
    private String title;
    private String description;
    private String imageUrl;
    private Integer minimumPrice;
    private SaleStatus saleStatus;

    private UserDto seller; //seller
    private List<SuggestionResponse> suggestionList = new ArrayList<>();

    public static UsedGoodsDto fromEntity(UsedGoods entity) {
//        List<SuggestionDto> suggestionDtoList = new ArrayList<>();
//
//        if (!entity.getSuggestionList().isEmpty()) {
//            List<Suggestion> suggestions = entity.getSuggestionList();
//            if (!suggestions.isEmpty()) {
//                for (Suggestion s : suggestions) {
//                    suggestionDtoList.add(SuggestionDto.fromEntity(s));
//                }
//            }
//        }

        return UsedGoodsDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .imageUrl(entity.getImageUrl())
                .saleStatus(entity.getSaleStatus())
                .seller(UserDto.fromEntity(entity.getSeller()))
//                .suggestionList(suggestionDtoList)
                .build();
    }

}


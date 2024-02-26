package com.example.missiontshoppingmall.usedGoods.dto.response;

import com.example.missiontshoppingmall.usedGoods.dto.SuggestionDto;
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
public class UsedGoodsWithoutSeller {
    private String title;
    private String description;
    private String imageUrl;
    private Integer minimumPrice;
    private SaleStatus saleStatus;

    private List<SuggestionDto> suggestionList = new ArrayList<>();

    public static UsedGoodsWithoutSeller fromEntity(UsedGoods entity) {
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

        return UsedGoodsWithoutSeller.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .imageUrl(entity.getImageUrl())
                .saleStatus(entity.getSaleStatus())
//                .suggestionList(suggestionDtoList)
                .build();
    }
}

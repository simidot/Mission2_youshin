package com.example.missiontshoppingmall.usedGoods.dto;

import com.example.missiontshoppingmall.usedGoods.entity.PurchaseStatus;
import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import com.example.missiontshoppingmall.usedGoods.entity.SuggestionStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuggestionDto {
    private Long suggestionId;
    private String suggestionMessage;
    private SuggestionStatus suggestionStatus;
    private PurchaseStatus purchaseStatus;
    private UserDto buyer;

    public static SuggestionDto fromEntity(Suggestion entity) {
        return SuggestionDto.builder()
                .suggestionId(entity.getId())
                .suggestionMessage(entity.getSuggestionMessage())
                .suggestionStatus(entity.getSuggestionStatus())
                .purchaseStatus(entity.getPurchaseStatus())
                .buyer(UserDto.fromEntity(entity.getBuyer()))
                .build();
    }
}

package com.example.missiontshoppingmall.usedGoods.dto.response;

import com.example.missiontshoppingmall.usedGoods.dto.UserDto;
import com.example.missiontshoppingmall.usedGoods.entity.PurchaseStatus;
import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import com.example.missiontshoppingmall.usedGoods.entity.SuggestionStatus;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuggestionResponse {
    private Long suggestionId;
    private String suggestionMessage;
    private SuggestionStatus suggestionStatus;
    private PurchaseStatus purchaseStatus;
    private UserDto buyer;

    public static SuggestionResponse fromEntity(Suggestion entity) {
        return SuggestionResponse.builder()
                .suggestionId(entity.getId())
                .suggestionMessage(entity.getSuggestionMessage())
                .suggestionStatus(entity.getSuggestionStatus())
                .purchaseStatus(entity.getPurchaseStatus())
                .buyer(UserDto.fromEntity(entity.getBuyer()))
                .build();
    }
}

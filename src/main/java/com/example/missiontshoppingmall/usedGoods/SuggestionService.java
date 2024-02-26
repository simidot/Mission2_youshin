package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.usedGoods.dto.request.SuggestionDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import com.example.missiontshoppingmall.usedGoods.entity.SuggestionStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.repo.SuggestionRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuggestionService {
    private final SuggestionRepo suggestionRepo;
    private final UsedGoodsRepo usedGoodsRepo;
    private final CustomUserDetailsManager manager;
    private final EntityFromOptional optional;



    // 물품에 대한 구매 제안 등록
    // 물품 등록한 사용자와 비활성사용자 제외! 등록가능
    public SuggestionResponse uploadSuggestion(Long usedGoodsId, SuggestionDto dto) {
        // usedGoodsId로 해당 물품을 찾고, 해당물품의 Seller와 같지 않아야 제안이 가능하다.
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);
        manager.checkIdIsNotEqual(foundGoods.getSeller().getAccountId()); // 같을시 예외발생함

        // 구매제안 등록하는 사람 추출
        UserEntity buyer = manager.loadUserFromAuth();

        Suggestion newSuggestion = Suggestion.builder()
                .usedProduct(foundGoods)
                .suggestionMessage(dto.getSuggestionMessage())
                .suggestionStatus(SuggestionStatus.WAIT)
                .buyer(buyer)
                .build();
        suggestionRepo.save(newSuggestion);
        return SuggestionResponse.fromEntity(newSuggestion);
    }


    // 물품 구매 제안 조회
    // 물품 등록한 사용자와 제안등록 사용자만 조회 가능
    // 제안등록자는 자신의 제안만 확인 가능
    // 물품등록자는 모든 제안 확인 가능

    // 물품 구매 제안에 대한 수락 또는 거절
    // 물품등록자는 수락/거절 > 구매제안 상태 수락/거절로 바뀜

    // 물품 구매 제안자가 구매 확정
    // 구매확정시 구매제안 상태는 확정상태
    // 구매확정 > 물품 상태 판매완료 & 다른 구매제안 상태 거절로 바뀜
}

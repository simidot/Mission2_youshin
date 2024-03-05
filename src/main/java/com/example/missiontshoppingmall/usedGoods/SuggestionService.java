package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.utils.EntityFromOptional;
import com.example.missiontshoppingmall.usedGoods.dto.request.SuggestionDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import com.example.missiontshoppingmall.usedGoods.entity.*;
import com.example.missiontshoppingmall.usedGoods.repo.SuggestionRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
                .suggestionMessage(dto.getSuggestionMessage())
                .suggestionStatus(SuggestionStatus.WAIT)
                .purchaseStatus(PurchaseStatus.NOT_CONFIRMED)
                .buyer(buyer)
                .build();
        newSuggestion.addUsedGoods(foundGoods);
        suggestionRepo.save(newSuggestion);
        return SuggestionResponse.fromEntity(newSuggestion);
    }


    // 물품 구매 제안 조회
    // 물품 등록한 사용자와 제안등록 사용자만 조회 가능
    public List<SuggestionResponse> readSuggestions(Long usedGoodsId) {
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);
        UserEntity loginUser = manager.loadUserFromAuth();

        // 물품등록자는 모든 제안 확인 가능
        if (loginUser.equals(foundGoods.getSeller())) {
            log.info("중고물품 등록자가 조회합니다");
            return suggestionRepo.findByUsedGoods(foundGoods).stream()
                    .map(SuggestionResponse::fromEntity)
//                    .peek(response -> log.info("SuggestionList:: " + response))
                    .collect(Collectors.toList());
        } // 제안등록자는 자신의 제안만 확인 가능
        else {
            List<Suggestion> suggestions = foundGoods.getSuggestionList()
                    .stream()
                    .filter(suggestion -> loginUser.equals(suggestion.getBuyer()))
                    .peek(suggestion -> log.info("중고물품 구매 제안자가 조회합니다."))
                    .collect(Collectors.toList());
            // 만약 자신의 제안이 없다면 suggestionList가 비어있을 것.
            if (suggestions.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "제안조회 권한이 없습니다.");
            }
            return suggestions.stream()
                    .map(SuggestionResponse::fromEntity)
                    .peek(response -> log.info("SuggestionList:: "+response))
                    .collect(Collectors.toList());
        }
    }

    // 물품 구매 제안에 대한 수락 또는 거절
    // 물품등록자는 수락/거절 > 구매제안 상태 수락/거절로 바뀜
    public SuggestionResponse acceptOrNot(Long usedGoodsId, Long suggestionId, boolean acceptance) {
        // 1. 물품을 등록한 사용자와 로그인한 사용자가 같은지 확인하기
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);
        manager.checkIdIsEqual(foundGoods.getSeller().getAccountId()); // 등록자만 가능함!

        // 물품제안자가 고른 제안
        Suggestion selectedSuggestion = optional.getSuggestion(suggestionId);

        // 그 제안의 SuggestionStatus가 WAIT인 상태일때만 가능
        if (!selectedSuggestion.getSuggestionStatus().equals(SuggestionStatus.WAIT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 물품등록자가 선택한 결과가 true면 수락, false면 거절
        if (acceptance) {
            selectedSuggestion.setSuggestionStatus(SuggestionStatus.ACCEPTED);
        } else {
            selectedSuggestion.setSuggestionStatus(SuggestionStatus.REJECTED);
        }
        suggestionRepo.save(selectedSuggestion);
        return SuggestionResponse.fromEntity(selectedSuggestion);
    }

    // 물품 구매 제안자가 구매 확정
    // 구매확정시 구매제안 상태는 확정상태
    // 구매확정 > 물품 상태 판매완료 & 다른 구매제안 상태 거절로 바뀜
    @Transactional
    public SuggestionResponse confirmOrNot(Long usedGoodsId, Long suggestionId, boolean confirmation) {
        // 1. 구매제안자인지 확인
        Suggestion thisSuggestion = optional.getSuggestion(suggestionId);
        manager.checkIdIsEqual(thisSuggestion.getBuyer().getAccountId());

        // 2. 구매제안의 판매자가 제안을 수락했는지 확인
        if (!thisSuggestion.getSuggestionStatus().equals(SuggestionStatus.ACCEPTED)) {
            log.info("판매자가 제안을 수락한 상태가 아닙니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 해당 중고물품을 찾고
        UsedGoods usedGoods = optional.getUsedGoods(usedGoodsId);
        // 2. 구매제안자 의견에 따라 구매확정 or 구매확정 취소
        if (confirmation) {
            // 구매확정시 해당 제안의 상태는 확정으로 변경.
            thisSuggestion.setPurchaseStatus(PurchaseStatus.CONFIRMED);
            // 확정시 나머지 제안의 구매제안 상태를 모두 거절로 바꿈.
            List<Suggestion> suggestions = suggestionRepo.findByUsedGoodsAndSuggestionStatus(usedGoods,
                    SuggestionStatus.WAIT);
            suggestions.stream()
                    .forEach(sugg -> sugg.setSuggestionStatus(SuggestionStatus.REJECTED));
            suggestions.stream()
                    .forEach(sugg -> suggestionRepo.save(sugg));
            // 구매확정시 대상 물품의 상태는 판매완료
            usedGoods.setSaleStatus(SaleStatus.SOLD);
        } else {
            // 확정 취소시...? 이 구매제안자 PurchaseStatus를 구매확정 취소로 바꾸고, 구매제안도 cancelled로 바꿈
            thisSuggestion.setPurchaseStatus(PurchaseStatus.CONFIRM_CANCEL);
            thisSuggestion.setSuggestionStatus(SuggestionStatus.CANCELED);
            // 나머지 구매제안들은 이미 wait인 상태므로 굳이 바꿀 필요가 없다.
        }
        suggestionRepo.save(thisSuggestion);
        usedGoodsRepo.save(usedGoods);
        return SuggestionResponse.fromEntity(thisSuggestion);
    }

    // todo: 물품 구매 확정시 두 사용자가 거래진행 위치를 제안.
//    public void suggestLocation(Long suggestionId) {
//
//    }
}

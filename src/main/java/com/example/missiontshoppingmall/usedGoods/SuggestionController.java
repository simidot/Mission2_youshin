package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.usedGoods.dto.request.SuggestionDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/used-goods/{usedGoodsId}/suggestion")
public class SuggestionController {
    private final SuggestionService suggestionService;

    // 물품에 대한 구매 제안 등록
    @PostMapping
    public SuggestionResponse uploadSuggestion(
            @PathVariable("usedGoodsId") Long usedGoodsId,
            @RequestBody SuggestionDto dto
    ) {
        return suggestionService.uploadSuggestion(usedGoodsId, dto);
    }

    // 물품 구매 제안 조회 (Seller&Buyer)
    @GetMapping
    public List<SuggestionResponse> readSuggestions(
            @PathVariable("usedGoodsId") Long usedGoodsId
    ) {
        return suggestionService.readSuggestions(usedGoodsId);
    }

    // 물품 구매 제안에 대한 수락 또는 거절 (Seller)
    @PostMapping("/{suggestionId}/acceptance")
    public SuggestionResponse acceptSuggestionsOrNot(
            @PathVariable("usedGoodsId") Long usedGoodsId,
            @PathVariable("suggestionId") Long suggestionId,
            @RequestParam("acceptance") boolean acceptance
    ) {
        return suggestionService.acceptOrNot(usedGoodsId, suggestionId, acceptance);
    }

    // TODO: 물품구매 제안자가 제안에 대한 전체조회도 가능하게 (?)

    // 물품 구매 제안자가 구매 확정 (Buyer)
    @PostMapping("/{suggestionId}/purchase-confirm")
    public SuggestionResponse confirmPurchase(
            @PathVariable("usedGoodsId") Long usedGoodsId,
            @PathVariable("suggestionId") Long suggestionId,
            @RequestParam("confirmation") boolean confirmation
    ) {
        return suggestionService.confirmOrNot(usedGoodsId, suggestionId, confirmation);
    }

//    todo: 물품 구매 확정시 두 사용자가 거래진행 위치를 제안.
//    @PostMapping("/{suggestionId}/location")
//    public void suggestLocation(
//            @PathVariable("usedGoodsId") Long usedGoodsId,
//            @PathVariable("suggestionId") Long suggestionId
//
//    ) {
//        return
//    }
}

package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.usedGoods.dto.request.SuggestionDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.SuggestionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    // 물품 구매 제안 조회
//    @GetMapping

    // 물품 구매 제안에 대한 수락 또는 거절

    // 물품 구매 제안자가 구매 확정

}

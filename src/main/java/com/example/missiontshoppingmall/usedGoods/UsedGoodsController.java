package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.AuthenticationFacade;
import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.UsedGoodsWithoutSeller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/used-goods")
public class UsedGoodsController {
    private final UsedGoodsService usedGoodsService;
    private final AuthenticationFacade facade;

    // 물품등록
    @PostMapping
    public UsedGoodsDto registerUsedGoods(
            @RequestBody UsedGoodsDto dto
    ) {
        return usedGoodsService.uploadUsedGoods(dto);
    }

    // 중고거래 등록된 물품 전체조회
    @GetMapping
    public List<UsedGoodsWithoutSeller> readAllGoods() {
        return usedGoodsService.readAllGoods();
    }

    // 중고거래 등록된 물품 단일조회
    @GetMapping("/{usedGoodsId}")
    public UsedGoodsWithoutSeller readOneGoods(
            @PathVariable("usedGoodsId") Long usedGoodsId
    ) {
        return usedGoodsService.readOneGoods(usedGoodsId);
    }

    // 중고거래 등록 물품 수정
    @PutMapping("/{usedGoodsId}")
    public UsedGoodsDto updateUsedGoods(
            @PathVariable("usedGoodsId") Long id,
            @RequestBody UsedGoodsDto dto
    ) {
        return usedGoodsService.updateUsedGoods(id, dto);
    }

    // 중고거래 등록한 물품 삭제
    @DeleteMapping("/{usedGoodsId}")
    public String deleteUsedGoods(
            @PathVariable("usedGoodsId") Long id
    ) {
        usedGoodsService.deleteUsedGoods(id);
        return "중고물품 삭제 완료";
    }

}

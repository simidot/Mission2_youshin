package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.shoppingMall.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/shopping-malls")
public class ShoppingMallController {
    private final ShoppingMallService mallService;

    // 쇼핑몰 개설
    // 사업자 사용자로 전환되며 준비중 상태의 쇼핑몰이 추가된 상태
    @PostMapping("/open-request")
    public MallOpenResponse openRequest(
            @RequestBody MallOpenRequest requestDto
    ) {
        return mallService.createOpenRequest(requestDto);
    }

    // 쇼핑몰 정보수정 (주인)
    @PutMapping("/{mallId}")
    public MallOpenResponse updateMall(
            @PathVariable("mallId") Long id,
            @RequestBody MallOpenRequest request
    ) {
        return mallService.updateMallInfo(id, request);
    }

    // 쇼핑몰 폐쇄요청 (주인)
    @PostMapping("/{mallId}/close-request")
    public MallCloseResponse closeMall(
            @PathVariable("mallId") Long id,
            @RequestBody MallCloseRequest dto
    ) {
        return mallService.closeRequest(id, dto);
    }

    // 쇼핑몰 조회 (비활성 사용자 제외한 사용자들) 전체 쇼핑몰 조회
    @GetMapping
    public List<MallInfoDto> allMalls() {
        return mallService.readAllMalls();
    }

    // 쇼핑몰 검색 (이름 mname, 대분류 cat 조건)
    @GetMapping("/search")
    public List<MallInfoDto> searchMalls(
            @RequestParam("q") String q,
            @RequestParam("criteria") String criteria
    ) {
        return mallService.searchMalls(q, criteria);
    }
}

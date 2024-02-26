package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/shopping-mall")
public class ShoppingMallController {
    private final ShoppingMallService mallService;

    // 쇼핑몰 개설
    // 사업자 사용자로 전환되며 준비중 상태의 쇼핑몰이 추가된 상태
    @PostMapping
    public MallOpenResponse openRequest(
            @RequestBody MallOpenRequest requestDto
    ) {
        return mallService.createOpenRequest(requestDto);
    }
}

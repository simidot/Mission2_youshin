package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.AuthenticationFacade;
import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            Authentication authentication,
            @RequestBody UsedGoodsDto dto
    ) {
        log.info("authenticion.getname(): "+authentication.getName());
        log.info("authorities:: "+authentication.getAuthorities().toString());
        log.info("class: "+authentication.getAuthorities().getClass());
        String accountId = authentication.getName();
//        if (authentication.getAuthorities().contains("USER_ACTIVE")) {
            return usedGoodsService.uploadUsedGoods(accountId, dto);
//        }
//        return null;
    }
}

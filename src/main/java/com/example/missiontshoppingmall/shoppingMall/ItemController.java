package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.shoppingMall.dto.ItemRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/shopping-mall/{mallId}")
public class ItemController {
    private final ItemService itemService;

    // 쇼핑몰 상품 등록 (주인)
    @PostMapping
    public ItemResponse uploadItem(
            @PathVariable("mallId") Long id,
            @RequestBody ItemRequest dto
    ) {
        return itemService.createItem(id, dto);
    }

    // 상품 수정
    @PutMapping("/items/{itemId}")
    public ItemResponse updateItem(
            @PathVariable("mallId") Long mallId,
            @PathVariable("itemId") Long itemId,
            @RequestBody ItemRequest dto
    ) {
        return itemService.updateItem(mallId, itemId, dto);
    }

    // 상품 삭제
    @DeleteMapping("/items/{itemId}")
    public String deleteItem(
            @PathVariable("mallId") Long mallId,
            @PathVariable("itemId") Long itemId
    ) {
        itemService.deleteItem(mallId, itemId);
        return "상품 삭제 완료";
    }


}

package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemResponse;
import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.shoppingMall.repo.ItemRepository;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final EntityFromOptional optional;
    private final CustomUserDetailsManager manager;

    // 상품 등록
    public ItemResponse createItem(Long mallId, ItemRequest dto) {
        // 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());
        Item newItem = Item.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .stock(dto.getStock())
                .mediumCategory(dto.getMediumCategory())
                .smallCategory(dto.getSmallCategory())
                .shoppingMall(mall)
                .build();
        itemRepository.save(newItem);
        return ItemResponse.fromEntity(newItem);
    }

    // 상품 수정
    public ItemResponse updateItem(Long mallId, Long itemId, ItemRequest dto) {
        // 쇼핑몰 주인인지 확인
        manager.checkIdIsNotEqual(optional.getMall(mallId).getOwner().getAccountId());
        // 상품 찾기
        Item foundItem = optional.getItem(itemId);
        log.info("foundItem:: "+foundItem.getName());
        // 수정사항 반영
        foundItem.setName(dto.getName());
        foundItem.setDescription(dto.getDescription());
        foundItem.setImage(dto.getImage());
        foundItem.setPrice(dto.getPrice());
        foundItem.setStock(dto.getStock());
        foundItem.setMediumCategory(dto.getMediumCategory());
        foundItem.setSmallCategory(dto.getSmallCategory());
        itemRepository.save(foundItem);
        return ItemResponse.fromEntity(foundItem);
    }

    // 상품 삭제
    public void deleteItem(Long mallId, Long itemId) {
        // 쇼핑몰 주인인지 확인
        manager.checkIdIsEqual(optional.getMall(mallId).getOwner().getAccountId());
        // 상품 찾기
        Item foundItem = optional.getItem(itemId);
        // 상품 삭제
        itemRepository.delete(foundItem);
    }

}

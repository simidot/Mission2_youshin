package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemInfoDto;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.ItemResponse;
import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.shoppingMall.repo.ItemRepository;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

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
        // 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());
        // 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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
        // 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());
        // 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 상품 찾기
        Item foundItem = optional.getItem(itemId);
        // 상품 삭제
        itemRepository.delete(foundItem);
    }

    // 상품 검색 (이름, 가격범위 기준으로 검색)
    public List<ItemInfoDto> searchItem(Long mallId, String q, String category) {
        // mall이 있는 몰인지 확인
        ShoppingMall foundMall = optional.getMall(mallId);

        List<Item> items = new ArrayList<>();
        // category가 iname이면 상품이름으로 검색
        if (category.equals("iname")) {
            items = itemRepository.findByShoppingMallAndNameContainingIgnoreCase(foundMall, q);
        }
        // categroy가 price면 가격범위 기준으로 검색
        // 가격범위일때는 (최소가격, 최대가격)이 String 형식으로 주어진다고 하였다.
        else if (category.equals("price")) {
            //최소가격
            int minPrice = Integer.parseInt(q.split(",")[0]);
            int maxPrice = Integer.parseInt(q.split(",")[1]);
            items = itemRepository.findByShoppingMallAndPriceBetween(foundMall, minPrice, maxPrice);
        }
        // 그 외면 예외 발생
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return items.stream().map(ItemInfoDto::fromEntity)
                .collect(Collectors.toList());
    }

}

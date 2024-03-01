package com.example.missiontshoppingmall.item;

import com.example.missiontshoppingmall.utils.EntityFromOptional;
import com.example.missiontshoppingmall.utils.S3FileService;
import com.example.missiontshoppingmall.item.dto.ItemInfoDto;
import com.example.missiontshoppingmall.item.dto.ItemRequest;
import com.example.missiontshoppingmall.item.dto.ItemResponse;
import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.item.entity.ItemImage;
import com.example.missiontshoppingmall.item.repo.ItemImageRepo;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.item.repo.ItemRepository;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
    private final S3FileService s3FileService;
    private final ItemImageRepo imageRepo;

    // 상품 등록
    public ItemResponse createItem(List<MultipartFile> multipartFile, Long mallId, ItemRequest dto) {
        // 1. 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());

        // 2. 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // 3. dto로 Item 새로운 객체 생성
        Item newItem = Item.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .stock(dto.getStock())
                .imageList(new ArrayList<>())
                .mediumCategory(dto.getMediumCategory())
                .smallCategory(dto.getSmallCategory())
                .shoppingMall(mall)
                .build();
        // 4. 아이템 저장
        newItem = itemRepository.save(newItem);

        // 5. S3에 이미지 업로드, 그 파일들이 저장된 url을 String으로 변환
        List<String> urls = s3FileService.uploadIntoS3("/item", multipartFile);
        // 5. 이 url 하나하나 ItemImage 객체가 생긴다 + 저장
        for (String url : urls) {
            ItemImage image = ItemImage.builder()
                    .imgUrl(url)
                    .build();
            image.addItem(newItem);
            imageRepo.save(image);
        }
        return ItemResponse.fromEntity(newItem);
    }

    // 상품 수정
    public ItemResponse updateItem(List<MultipartFile> multipartFile, Long mallId, Long itemId, ItemRequest dto) {
        // 1. 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());
        // 2. 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 3. 상품 찾기
        Item foundItem = optional.getItem(itemId);
        log.info("foundItem:: "+foundItem.getName());
        // 4. 수정사항 반영
        foundItem.setName(dto.getName());
        foundItem.setDescription(dto.getDescription());
        foundItem.setPrice(dto.getPrice());
        foundItem.setStock(dto.getStock());
        foundItem.setMediumCategory(dto.getMediumCategory());
        foundItem.setSmallCategory(dto.getSmallCategory());
        foundItem = itemRepository.save(foundItem);

        // 5. 사진이 추가되면 새로운 Image 엔티티가 생성됨.
        List<String> urls = s3FileService.uploadIntoS3("/item", multipartFile);
        for (String url : urls) {
            ItemImage image = ItemImage.builder()
                    .imgUrl(url)
                    .build();
            image.addItem(foundItem);
            imageRepo.save(image);
        }
        return ItemResponse.fromEntity(foundItem);
    }

    // 상품 삭제
    public void deleteItem(Long mallId, Long itemId) {
        // 1. 로그인사용자가 쇼핑몰 주인인지 확인
        ShoppingMall mall = optional.getMall(mallId);
        manager.checkIdIsEqual(mall.getOwner().getAccountId());
        // 2. 쇼핑몰이 OPEN인지 확인
        if (!mall.getRunningStatus().equals(RunningStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 3. 상품 찾기
        Item foundItem = optional.getItem(itemId);
        // 4. 상품을 삭제하면 상품이미지도 함께 삭제됨. 그 전에 S3에서 삭제
        for (ItemImage image : foundItem.getImageList()) {
            s3FileService.deleteImage("/item", image.getImgUrl());
        }
        // 5. 상품 삭제
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

package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.repo.SuggestionRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.CustomUserDetails;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsedGoodsService {
    private final UsedGoodsRepo usedGoodsRepo;
    private final SuggestionRepo suggestionRepo;
    private final UserRepository userRepository;
    private final CustomUserDetailsManager manager;

    // 중고물품 업로드
    public UsedGoodsDto uploadUsedGoods(String accountId, UsedGoodsDto dto) {
        Optional<UserEntity> optionalEntity = userRepository.findByAccountId(accountId);
        if (optionalEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity seller = optionalEntity.get();
        UsedGoods newGoods = UsedGoods.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .minimumPrice(dto.getMinimumPrice())
                .imageUrl(dto.getImageUrl())
                .saleStatus(SaleStatus.ON_SALE)
                .seller(seller)
                .build();
        newGoods = usedGoodsRepo.save(newGoods);
        return UsedGoodsDto.fromEntity(newGoods);
    }

    // 중고거래 등록된 물품 전체조회
    public List<UsedGoodsDto> readAllGoods(String accountId) {
        log.info("readAllGoods: "+accountId);
        CustomUserDetails user = manager.loadUserByUsername(accountId);
        List<UsedGoodsDto> response = new ArrayList<>();
//        if (user.getStringAuthorities().contains("ROLE_ACTIVE")) {
            List<UsedGoods> usedGoods = usedGoodsRepo.findAll();
            for (UsedGoods g : usedGoods) {
                response.add(UsedGoodsDto.fromEntity(g));
//            }
        }
        return response;
    }

    // 중고거래 등록된 물품 단일조회

    // 중고거래 등록 물품 수정
    public UsedGoodsDto updateUsedGoods(Long id, UsedGoodsDto dto) {
        Optional<UsedGoods> usedGoods = usedGoodsRepo.findById(id);
        if (usedGoods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UsedGoods foundGoods = usedGoods.get();

        // 로그인한 아이디와 물품 등록 판매자가 다르면 unauthorized
        String sellerId = foundGoods.getSeller().getAccountId();
        manager.checkIdIsEqual(sellerId);

        foundGoods.setTitle(dto.getTitle());
        foundGoods.setDescription(dto.getDescription());
        foundGoods.setMinimumPrice(dto.getMinimumPrice());
        foundGoods.setImageUrl(dto.getImageUrl());

        return UsedGoodsDto.fromEntity(foundGoods);
    }



}

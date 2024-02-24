package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.repo.SuggestionRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsedGoodsService {
    private final UsedGoodsRepo usedProductRepo;
    private final SuggestionRepo suggestionRepo;
    private final UserRepository userRepository;

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
        newGoods = usedProductRepo.save(newGoods);
        return UsedGoodsDto.fromEntity(newGoods);
    }

}

package com.example.missiontshoppingmall;

import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.repo.SuggestionRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntityFromOptional {
    private final UsedGoodsRepo usedGoodsRepo;
    private final UserRepository userRepository;
    private final SuggestionRepo suggestionRepo;

    // 아래에서 계속 반복되는 Optional에서 중고물품 엔티티 반환 메서드
    public UsedGoods getUsedGoods(Long usedGoodsId) {
        Optional<UsedGoods> optionalUsedGoods = usedGoodsRepo.findById(usedGoodsId);
        if (optionalUsedGoods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalUsedGoods.get();
    }

    // 아래에서 계속 반복되는 Optional에서 사용자 엔티티 반환 메서드
    public UserEntity getFoundUser(String accountId) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalUser.get();
    }

}

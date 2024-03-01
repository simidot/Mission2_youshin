package com.example.missiontshoppingmall.utils;

import com.example.missiontshoppingmall.item.entity.Item;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.order.repo.OrderRepository;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.item.repo.ItemRepository;
import com.example.missiontshoppingmall.shoppingMall.repo.ShoppingMallRepo;
import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
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
    private final ShoppingMallRepo mallRepo;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

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

    public Suggestion getSuggestion(Long suggestionId) {
        Optional<Suggestion> optionalSuggestion = suggestionRepo.findById(suggestionId);
        if (optionalSuggestion.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalSuggestion.get();
    }

    public ShoppingMall getMall(Long mallId) {
        Optional<ShoppingMall> optionalShoppingMall = mallRepo.findById(mallId);
        if (optionalShoppingMall.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalShoppingMall.get();
    }

    public Item getItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalItem.get();
    }

    public ItemOrder gerOrder(Long orderId) {
        Optional<ItemOrder> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalOrder.get();
    }

}

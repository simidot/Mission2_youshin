package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByShoppingMallAndNameContainingIgnoreCase(ShoppingMall mall, String name);

    List<Item> findByShoppingMallAndPriceBetween(ShoppingMall mall, Integer min, Integer max);
}

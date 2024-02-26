package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}

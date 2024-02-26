package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingMallRepo extends JpaRepository<ShoppingMall, Long> {

}

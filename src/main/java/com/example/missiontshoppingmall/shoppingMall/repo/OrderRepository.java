package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

package com.example.missiontshoppingmall.order.repo;

import com.example.missiontshoppingmall.order.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ItemOrder, Long> {

}

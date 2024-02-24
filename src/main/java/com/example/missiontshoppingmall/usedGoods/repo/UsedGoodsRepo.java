package com.example.missiontshoppingmall.usedGoods.repo;

import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedGoodsRepo extends JpaRepository<UsedGoods, Long> {
}

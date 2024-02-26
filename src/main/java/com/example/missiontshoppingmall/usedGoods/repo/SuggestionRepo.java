package com.example.missiontshoppingmall.usedGoods.repo;

import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByBuyer(UserEntity buyer);

    List<Suggestion> findByUsedGoods(UsedGoods goods);

}

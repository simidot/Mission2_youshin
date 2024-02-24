package com.example.missiontshoppingmall.usedGoods.repo;

import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {

}

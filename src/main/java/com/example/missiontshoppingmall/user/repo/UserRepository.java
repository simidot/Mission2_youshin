package com.example.missiontshoppingmall.user.repo;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAccountId(String userId);

    boolean existsByAccountId(String userId);
}

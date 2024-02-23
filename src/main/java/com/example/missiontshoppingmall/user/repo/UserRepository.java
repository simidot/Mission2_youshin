package com.example.missiontshoppingmall.user.repo;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //계정아이디로 찾기
    Optional<UserEntity> findByAccountId(String userId);
    //계정아이디로 존재여부 확인
    boolean existsByAccountId(String userId);
    //사업자번호가 null이 아닌 사람들 보기
    List<UserEntity> findByBusinessNumberIsNotNullOrderByUpdatedAtDesc();


}

package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.*;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingMallRepo extends JpaRepository<ShoppingMall, Long> {

    // 쇼핑몰 주인이 가진 쇼핑몰 모두 찾기
    List<ShoppingMall> findByOwner(UserEntity owner);

    // 요청타입, 운영상태, 허가상태로 쇼핑몰 찾기
    List<ShoppingMall> findByRequestTypeAndRunningStatusAndAllowance(
            RequestType requestType, RunningStatus runningStatus, Allowance allowance
    );

    // 요청타입, 운영상태, 폐쇄이유로 쇼핑몰 찾기
    List<ShoppingMall> findByRequestTypeAndRunningStatusAndCloseReasonIsNotNull(
            RequestType requestType, RunningStatus runningStatus
    );

    // 운영상태로 찾기 / 업데이트 최신순으로 정렬
    List<ShoppingMall> findByRunningStatusOrderByRecentOrderDateDesc(RunningStatus status);

    // 쇼핑몰 이름으로 검색, 운영상태는 open
    List<ShoppingMall> findByNameContainingIgnoreCaseAndRunningStatus(String q, RunningStatus status);

    // 쇼핑몰 카테고리로 검색, 운영상태틑 open
    List<ShoppingMall> findByLargeCategoryAndRunningStatus(LargeCategory q, RunningStatus status);



}

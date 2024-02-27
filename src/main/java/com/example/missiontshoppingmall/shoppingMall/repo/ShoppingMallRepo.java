package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingMallRepo extends JpaRepository<ShoppingMall, Long> {
    List<ShoppingMall> findByRequestTypeAndRunningStatusAndOpenIsAllowedIsNull(
            RequestType requestType, RunningStatus runningStatus);

    List<ShoppingMall> findByRequestTypeAndRunningStatusAndCloseReasonIsNotNull(
            RequestType requestType, RunningStatus runningStatus
    );

}

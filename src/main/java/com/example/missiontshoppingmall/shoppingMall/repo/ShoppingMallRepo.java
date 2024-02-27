package com.example.missiontshoppingmall.shoppingMall.repo;

import com.example.missiontshoppingmall.shoppingMall.entity.Allowance;
import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingMallRepo extends JpaRepository<ShoppingMall, Long> {
    List<ShoppingMall> findByOwner(UserEntity owner);
    List<ShoppingMall> findByRequestTypeAndRunningStatusAndAllowance(
            RequestType requestType, RunningStatus runningStatus, Allowance allowance
    );

    List<ShoppingMall> findByRequestTypeAndRunningStatusAndCloseReasonIsNotNull(
            RequestType requestType, RunningStatus runningStatus
    );

}

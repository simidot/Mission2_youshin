package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.admin.dto.BAManagement;
import com.example.missiontshoppingmall.shoppingMall.entity.RunningStatus;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.shoppingMall.repo.ShoppingMallRepo;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final ShoppingMallRepo mallRepo;
    private final EntityFromOptional optional;

    // 관리자의 사업자 전환신청 거절/수락 기능
    public BAResponse manageBusinessAccount(String accountId, BAManagement agreement) {
        // 계정id로 찾고,
        UserEntity foundUser = optional.getFoundUser(accountId);
        // 신청자는 active상태
        String authorities = foundUser.getAuthority();
        // 관리자가 승인한 경우
        if (agreement.isBusinessAllowance()) {
            // 사용자 권한과 사업자허가여부 속성을 변경 & update
            authorities = "ROLE_BUSINESS";
            foundUser.setBusinessIsAllowed(true);
            foundUser.setAuthority(authorities);
            // 즉시 해당 사용자가 주인인 쇼핑몰이 추가됨 (준비중 상태)
            ShoppingMall newMall = ShoppingMall.builder()
                    .runningStatus(RunningStatus.READY)
                    .owner(foundUser)
                    .build();

            userRepository.save(foundUser);
            mallRepo.save(newMall);
        }
        // 승인이 거절된 경우는 본래 UserEntity에 저장된 결과로 반환

        // 반환타입을 맞춰 return
        return BAResponse.builder()
                .accountId(accountId)
                .authority(authorities)
                .businessNumber(foundUser.getBusinessNumber())
                .businessIsAllowed(foundUser.getBusinessIsAllowed())
                .build();
    }

    // 사업자 전환신청 전체조회
    public List<BAResponse> findAllBARequests() {
        List<UserEntity> userEntityList = userRepository.findByBusinessNumberIsNotNullOrderByUpdatedAtDesc();
        List<BAResponse> responseList = new ArrayList<>();
        for (UserEntity entity : userEntityList) {
            responseList.add(BAResponse.fromEntity(entity));
        }
        return responseList;
    }

    // 사업자 전환신청 단일조회
    public BAResponse findOneBARequest(String accountId) {
        UserEntity foundUser = optional.getFoundUser(accountId);
        return BAResponse.fromEntity(foundUser);
    }

}

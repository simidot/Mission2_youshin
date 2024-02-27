package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.admin.dto.*;
import com.example.missiontshoppingmall.shoppingMall.dto.MallCloseResponse;
import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenResponse;
import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
                .id(foundUser.getId())
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

    // 개설신청된 쇼핑몰 목록 전체조회
    public List<MallOpenResponse> readAllOpenRequest() {
        // 처음 신청할때에는 runningStatus가 READY이고,
        // 이때 요청타입이 OPEN이며,
        // openIsAllowed가 null일때 쇼핑몰 개설신청이다.
        return mallRepo.findByRequestTypeAndRunningStatusAndOpenIsAllowedIsNull(RequestType.OPEN, RunningStatus.READY)
                .stream()
                .map(MallOpenResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 개설신청된 쇼핑몰 목록 단일조회
    public MallOpenResponse readOneOpenRequest(Long id) {
        ShoppingMall mall = optional.getMall(id);
        return MallOpenResponse.fromEntity(mall);
    }

    // 개설신청된 쇼핑몰 허가/불허
    public MallOpenResult allowOpenOrNot(Long id, MallOpenManagement management) {
        ShoppingMall mall = optional.getMall(id);
        // 허가한 경우
        if (management.isAllowed()) {
            mall.setRunningStatus(RunningStatus.OPEN);
            mall.setOpenIsAllowed(true);
        } //불허한 경우
        else {
            mall.setOpenIsAllowed(false);
            mall.setDeniedReason(management.getDeniedReason());
        }
        mallRepo.save(mall);
        return MallOpenResult.fromEntity(mall);
    }

    // 폐쇄신청된 쇼핑몰 목록 전체조회
    public List<MallCloseResponse> readAllCloseRequest() {
        // 폐쇄신청할 때는 runningStatus가 OPEN이고,
        // RequestType이 CLOSE이고,
        // closeReason이 null이 아니여야함.
        return mallRepo.findByRequestTypeAndRunningStatusAndCloseReasonIsNotNull(RequestType.CLOSE,
                        RunningStatus.OPEN)
                .stream()
                .map(MallCloseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 폐쇄신청된 쇼핑몰 단일 조회
    public MallCloseResponse readOneCloseRequest(Long id) {
        ShoppingMall mall = optional.getMall(id);
        return MallCloseResponse.fromEntity(mall);
    }

    // 폐쇄신청 수락/거절
    public MallCloseResult allowCloseOrNot(Long id, MallCloseManagement management) {
        ShoppingMall mall = optional.getMall(id);
        // 허가한 경우
        if (management.isAllowed()) {
            mall.setRunningStatus(RunningStatus.CLOSE);
            mall.setCloseIsAllowed(true);
            mall.setDeniedReason(null);
        } //불허한 경우
        else {
            mall.setCloseIsAllowed(false);
            mall.setDeniedReason(management.getDeniedReason());
        }
        mallRepo.save(mall);
        return MallCloseResult.fromEntity(mall);
    }
}

package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.admin.dto.MallOpenResult;
import com.example.missiontshoppingmall.shoppingMall.dto.*;
import com.example.missiontshoppingmall.shoppingMall.entity.*;
import com.example.missiontshoppingmall.shoppingMall.repo.ShoppingMallRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingMallService {
    private final ShoppingMallRepo mallRepo;
    private final CustomUserDetailsManager manager;
    private final EntityFromOptional optional;

    // 쇼핑몰 개설 신청
    public MallOpenResponse createOpenRequest(MallOpenRequest dto) {
        // 1. 해당 사용자가 Business인지 확인
        UserEntity foundUser= manager.loadUserFromAuth();
        if (!foundUser.getAuthority().equals("ROLE_BUSINESS")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 2. 해당 사용자의 쇼핑몰을 꺼내옴.
        List<ShoppingMall> foundMalls = mallRepo.findByOwner(foundUser);

        // 3. dto 확인
        // 하나라도 null이면 false 반환, 모두 null이 아니면 true 반환
        boolean dtoCheck = Stream.of(dto.getName(), dto.getDescription(), dto.getLargeCategory())
                .allMatch(Objects::nonNull);

        // 4. 재신청 사업자인지 확인
        boolean isRequestNew = true; //true면 신규 신청, false면 재신청
        for (ShoppingMall mall : foundMalls) { // mall을 돌면서 재신청 사업자인 경우 찾기
            if (mall.getRunningStatus().equals(RunningStatus.READY)
                    && mall.getAllowance().equals(Allowance.OPEN_IS_REJECTED)) {
                log.info("재신청 사업자입니다. ");
                isRequestNew = false;
            }
        }
        ShoppingMall mall = foundMalls.get(foundMalls.size()-1); //마지막
        // 5. 신규신청이라면 & 필수항목 모두 채웠다면
        if (isRequestNew && dtoCheck) {
            mall.setName(dto.getName());
            mall.setDescription(dto.getDescription());
            mall.setLargeCategory(dto.getLargeCategory());
            mall.setRequestType(RequestType.OPEN);
            mallRepo.save(mall);
        } // 재신청이라면 & 필수항목 모두 채웠다면
        else if (!isRequestNew && dtoCheck) {
            mall = ShoppingMall.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .owner(foundUser)
                    .requestType(RequestType.OPEN)
                    .runningStatus(RunningStatus.READY)
                    .allowance(Allowance.WAIT)
                    .largeCategory(dto.getLargeCategory())
                    .build();
            mallRepo.save(mall);
        } // 신규신청, 재신청도 아니고, 필수항목 채우지 못했다면
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return MallOpenResponse.fromEntity(mall);
    }

    // 쇼핑몰 정보수정 (주인만 가능)
    public MallOpenResponse updateMallInfo(Long mallId, MallOpenRequest dto) {
        ShoppingMall foundMall = optional.getMall(mallId);
        // 이 쇼핑몰의 주인의 계정으로 로그인되어 있는지 확인. (다르면 예외처리)
        manager.checkIdIsEqual(foundMall.getOwner().getAccountId());
        // 이 쇼핑몰이 운영중인 쇼핑몰인지 확인
        if (!foundMall.getRunningStatus().equals(RunningStatus.CLOSE)) {
            foundMall.setName(dto.getName());
            foundMall.setDescription(dto.getDescription());
            foundMall.setLargeCategory(dto.getLargeCategory());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        mallRepo.save(foundMall);
        return MallOpenResponse.fromEntity(foundMall);
    }

    // 쇼핑몰 폐쇄요청 (주인만 가능)
    public MallCloseResponse closeRequest(Long mallId, MallCloseRequest dto) {
        ShoppingMall foundMall = optional.getMall(mallId);
        // 이 쇼핑몰의 주인인지 확인 (다르면 예외처리)
        manager.checkIdIsEqual(foundMall.getOwner().getAccountId());
        // 운영중인 쇼핑몰인지 확인
        if (foundMall.getRunningStatus().equals(RunningStatus.OPEN)) {
            //요청 유형을 close로 바꾸고, 이유를 넣는다 요청응답 대기중인 상태
            foundMall.setRequestType(dto.getRequestType());
            foundMall.setCloseReason(dto.getCloseReason());
            foundMall.setAllowance(Allowance.WAIT);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        mallRepo.save(foundMall);
        return MallCloseResponse.fromEntity(foundMall);
    }

    // 쇼핑몰 전체 조회
    public List<MallInfoDto> readAllMalls() {
        return mallRepo.findByRunningStatusOrderByUpdatedAtDesc(RunningStatus.OPEN)
                .stream()
                .map(MallInfoDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 쇼핑몰 검색 (카테고리에 따라서)
    public List<MallInfoDto> searchMalls(String q, String criteria) {
        List<ShoppingMall> malls = new ArrayList<>();
        // mname은 쇼핑몰 이름으로 검색
        if (criteria.equals("mname")) {
            malls = mallRepo.findByNameContainingIgnoreCaseAndRunningStatus(q, RunningStatus.OPEN);
        } // cat은 쇼핑몰 대분류로 검색
        else if (criteria.equals("cat")) {
            malls = mallRepo.findByLargeCategoryAndRunningStatus(LargeCategory.valueOf(q), RunningStatus.OPEN);
        }
        // 둘다 아니면 bad request
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return malls.stream().map(MallInfoDto::fromEntity)
                .collect(Collectors.toList());
    }

}
